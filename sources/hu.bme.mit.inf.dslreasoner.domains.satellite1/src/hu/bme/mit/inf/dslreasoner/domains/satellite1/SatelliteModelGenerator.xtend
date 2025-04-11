package hu.bme.mit.inf.dslreasoner.domains.satellite1

import satellite1.SatelliteFactory
import java.util.Random
import satellite1.Spacecraft
import satellite1.CommSubsystem
import satellite1.CubeSat3U
import satellite1.CubeSat6U
import javax.swing.text.StyleContext.SmallAttributeSet
import satellite1.SmallSat
import java.util.ArrayList
import java.util.List
import satellite1.KaCommSubsystem
import satellite1.XCommSubsystem
import satellite1.UHFCommSubsystem
import satellite1.InterferometryMission
import satellite1.GroundStationNetwork
import org.eclipse.emf.ecore.xml.namespace.SpaceType
import java.util.Collections
import java.util.function.Supplier
import java.util.stream.Stream
import java.util.Set
import satellite1.CommunicatingElement
import javax.sound.midi.SysexMessage

class SatelliteModelWrapper{
	public val SatelliteFactory factory
	public val InterferometryMission mission
	public var GroundStationNetwork gsn
	public var KaCommSubsystem gsnka
	public var XCommSubsystem gsnx
	
	def getSatComms(){
		return mission.spacecraft.stream
			.flatMap([sat | sat.commSubsystem.stream])
	}
	def getCrossKa(){
		return mission.spacecraft.stream
			.flatMap([sat | sat.commSubsystem.stream])
			.filter([it instanceof KaCommSubsystem])
			.map([it as KaCommSubsystem])
	}
	def getCrossX(){
		return mission.spacecraft.stream
			.flatMap([sat | sat.commSubsystem.stream])
			.filter([it instanceof XCommSubsystem])
			.map([it as XCommSubsystem])
	}
	def getCrossUHF(){
		return mission.spacecraft.stream
			.flatMap([sat | sat.commSubsystem.stream])
			.filter([it instanceof UHFCommSubsystem])
			.map([it as UHFCommSubsystem])
	}
	def getAllKa(){
		return Stream.concat(Stream.of(gsnka), crossKa)
	}
	def getAllX(){
		return Stream.concat(Stream.of(gsnx), crossX)
	}
	def Stream<CommSubsystem> incomingDirect(CommSubsystem com){
		return satComms.filter([src | src.target===com || src.fallback===com])
	}
	def Stream<CommSubsystem> incomingDirectAndRelay(CommSubsystem com){
		val container = com.eContainer as CommunicatingElement
		
		val connected = container.commSubsystem.stream
			.flatMap([trg | satComms.filter([src | src.target===com || src.fallback===com])])
			
		return Stream.concat(container.commSubsystem.stream.filter([it!==com]), connected)
	}
	def <T> Stream<T> ofType(Stream<?> source, T example) {
		return source.filter([it.class.equals(example.class)]).map([it as T])
	}
	def Set<CommSubsystem> incomingTransitiveDirectAndRelay(CommSubsystem trg, Set<CommSubsystem> buffer){
		if(buffer.contains(trg)){
			return buffer
		}
		buffer.add(trg)
		incomingDirectAndRelay(trg).forEach[src | incomingTransitiveDirectAndRelay(src, buffer)]
		return buffer
	}
	def <T extends CommSubsystem> Stream<T> linkableTo(T com){
		val incoming = incomingTransitiveDirectAndRelay(com, newHashSet)
		return Stream.of(allKa, allX, crossUHF).flatMap([it]).filter([it.class.equals(com.class)]).filter([it!==com]).map([it as T])
			.filter([!incoming.contains(it)])
	}
	def allTarget(){
		return mission.spacecraft.stream.flatMap([sat | sat.commSubsystem.stream])
			.flatMap([com | if(com.target!==null) Stream.of(com -> com.target) else Stream.of()])
	}
	def allFallback(){
		return mission.spacecraft.stream.flatMap([sat | sat.commSubsystem.stream])
			.flatMap([com | if(com.fallback!==null) Stream.of(com -> com.fallback) else Stream.of()])
	}
	def allLinks(){
		return Stream.concat(allTarget, allFallback)
	}
	def CommSubsystem transmitting(Spacecraft sat){
		return sat.commSubsystem.findFirst[
			it.target!==null || it.fallback!==null
		]
	}
	public var Random rnd
	
	new(){
		factory = SatelliteFactory.eINSTANCE
		mission = factory.createInterferometryMission
	}
	
	def int size(){
		var size = 0
		
		if(mission.groundStationNetwork !== null){
			size++;
			size = mission.groundStationNetwork.commSubsystem.fold(size, [old, _ | old+1])
		}
		return mission.spacecraft.fold(size, [old, spc|
			var value = old+1;
			if(spc.payload!==null){
				value++
			}
			value += spc.commSubsystem.size
			value
		])
	}
}
class SatelliteModelGenerator {
	def make(int component, long seed){
		val wrapper = new SatelliteModelWrapper
		
		/**
		 * Model minimum
		 */
		 wrapper.gsn = wrapper.factory.createGroundStationNetwork
		 wrapper.gsnka = wrapper.factory.createKaCommSubsystem
		 wrapper.gsnx = wrapper.factory.createXCommSubsystem
		 wrapper.gsn.commSubsystem.add(wrapper.gsnka)
		 wrapper.gsn.commSubsystem.add(wrapper.gsnx)
		 wrapper.mission.groundStationNetwork = wrapper.gsn
		 
		 var count = 0
		 wrapper.rnd = new Random
		 wrapper.rnd.seed = seed
		 
		return addToModel(wrapper, component)
	}
	def addToModel(SatelliteModelWrapper wrapper, int component){
		val factory = SatelliteFactory.eINSTANCE
		
		 var count = 0
		 while(count<component){
		 	/**
		 	 * Make satellite
		 	 */
		 	 val satcode = wrapper.rnd.nextInt(3)
		 	 var Spacecraft sat = null
		 	 switch(satcode){
		 	 	case 0:
		 	 		sat = factory.createCubeSat3U
		 	 	case 1:
		 	 		sat = factory.createCubeSat6U
		 	 	case 2:
		 	 		sat  = factory.createSmallSat
		 	 	default:
		 	 		throw new RuntimeException("Satellite typecode is out of bounds.")
		 	 }
		 	 /**
		 	  * Primary communication subsystem
		 	  */
		 	 var CommSubsystem primary = null
		 	 if(sat instanceof CubeSat3U){
		 	 	if(wrapper.crossUHF.count>=2){
		 	 		val commcode = wrapper.rnd.nextInt(2)
		 	 		if(commcode===0){
		 	 			primary = factory.createUHFCommSubsystem
		 	 			val comms = wrapper.crossUHF.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		} else{
		 	 			primary = factory.createXCommSubsystem
		 	 			val comms = wrapper.allX.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		}
		 	 	} else {
		 	 		primary = factory.createXCommSubsystem
		 	 		val comms = wrapper.allX.select2(wrapper.rnd)
		 	 		primary.target = comms.get(0)
		 	 		primary.fallback = comms.get(1)
		 	 	}
		 	 }
		 	 if(sat instanceof CubeSat6U){
		 	 	val commcode = wrapper.rnd.nextInt(2);//0<->XComm, 1<->UHFComm
		 	 	if(wrapper.crossUHF.count>=2 && commcode==1){
		 	 		primary = factory.createUHFCommSubsystem
		 	 		val comms = wrapper.crossUHF.select2(wrapper.rnd)
		 	 		primary.target = comms.get(0)
		 	 		primary.fallback = comms.get(1)
		 	 	} else {
		 	 		primary = factory.createXCommSubsystem
		 	 		if(wrapper.crossX.count>=2){
		 	 			val comms = wrapper.allX.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		} else {
		 	 			primary.target = wrapper.gsnx
		 	 			primary.fallback = wrapper.gsnx
		 	 		}
		 	 	}
		 	 }
		 	 if(sat instanceof SmallSat){
		 	 	val commcode = wrapper.rnd.nextInt(wrapper.crossUHF.count>=2?3:2)
		 	 	switch(commcode){
		 	 		case 0: {
		 	 			primary = factory.createXCommSubsystem()
		 	 			val comms = wrapper.allX.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		}	
		 	 		case 1:{
		 	 			primary = factory.createKaCommSubsystem
		 	 			val comms = wrapper.allKa.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		}
		 	 		case 2:{
		 	 			primary = factory.createUHFCommSubsystem
		 	 			val comms = wrapper.crossUHF.select2(wrapper.rnd)
		 	 			primary.target = comms.get(0)
		 	 			primary.fallback = comms.get(1)
		 	 		}
		 	 		default:
		 	 			throw new RuntimeException("CommSystem typecode if out of bounds.")
		 	 	}
		 	 }
		 	 /**
		 	  * Create secondary comm
		 	  */
		 	 var CommSubsystem seconary = null
		 	 val commcode = wrapper.rnd.nextInt(4)
		 	 if(commcode<2){
		 	 	if(commcode===0){
		 	 		seconary = factory.createXCommSubsystem
		 	 	} else {
		 	 		seconary = factory.createUHFCommSubsystem
		 	 	}
		 	 } else {
		 	 	if(commcode===2 && sat instanceof SmallSat){
		 	 		seconary = factory.createKaCommSubsystem
		 	 	}
		 	 }
		 	 /**
		 	  * Create payload
		 	  */
		 	 if(wrapper.rnd.nextInt(3)<2){
		 	 	sat.payload = factory.createInterferometryPayload
		 	 } 
		 	 /**
		 	  * Update model
		 	  */
		 	 wrapper.mission.spacecraft.add(sat)
		 	 count++
		 	 sat.commSubsystem.add(primary)count++
		 	 if(seconary!==null){
		 	 	sat.commSubsystem.add(seconary)
		 	 	count++
		 	 }
		 	 if(sat.payload!==null){
		 	 	count++
		 	 }
		 }
		 return wrapper
	}
	def <T extends CommSubsystem> List<T> select2(Stream<T> stream, Random random) {
		val objects = stream.toList
		if(objects.size==1){
			return #[objects.get(0),objects.get(0)]
		}
		val c1 = random.nextInt(objects.size)
		var c2 = random.nextInt(objects.size - 1)
		if(c2>=c1)
			c2++
		return #[objects.get(c1),objects.get(c2)]
	}
	
	def makeRandomChange(SatelliteModelWrapper wrapper, int changes){
		var count = 0
		while(count < changes){
			System.out.print("Action ")
			val options = #[ [|addPayload(wrapper)], [|removePayload(wrapper)],
				[|addSatellite(wrapper)], [| removeSatellite(wrapper)],
				[|addExtraComm(wrapper)], [| removeExtraComm(wrapper)],
				[|rerouteLink(wrapper)]
			]
			val action = options.get(wrapper.rnd.nextInt(options.size))
			if(action.apply){
				println(" succeeded. ("+(count+1) + " of "+changes+")")
				count++
			} else {
				println(" failed. ("+(count+1) + " of "+changes+")")
			}
		}
	}
	
	def boolean addPayload(SatelliteModelWrapper wrapper){
		System.out.print("addPayload")
		if(wrapper.size > 60){
			return false
		}
		val sats = newLinkedList
		sats.addAll(wrapper.mission.spacecraft.filter[sat | sat.payload===null])
		if(sats.isEmpty){
			return false
		}
		Collections.shuffle(sats, wrapper.rnd)
		sats.get(0).payload = wrapper.factory.createInterferometryPayload
		return true
	}
	def boolean removePayload(SatelliteModelWrapper wrapper){
		System.out.print("removePayload")
		val sats = wrapper.mission.spacecraft.stream.filter([sat | sat.payload !== null]).toList
		if(sats.size <= 2){
			return false
		}
		sats.get(wrapper.rnd.nextInt(sats.size)).payload = null
		return true
	}
	def boolean addSatellite(SatelliteModelWrapper wrapper){
		System.out.print("addSatellite")
		if(wrapper.size > 60-2){
			return false
		}
		val options = #[
			[| 
				if(wrapper.crossUHF.count<2){
					return false
				}
				val sat = wrapper.factory.createCubeSat3U
				val com = wrapper.factory.createUHFCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.crossUHF.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[|
				val sat = wrapper.factory.createCubeSat3U
				val com = wrapper.factory.createXCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.allX.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[| 
				if(wrapper.crossUHF.count<2){
					return false
				}
				val sat = wrapper.factory.createCubeSat6U
				val com = wrapper.factory.createUHFCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.crossUHF.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[|
				val sat = wrapper.factory.createCubeSat6U
				val com = wrapper.factory.createXCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.allX.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[|
				val sat = wrapper.factory.createSmallSat
				val com = wrapper.factory.createUHFCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.crossUHF.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[|
				val sat = wrapper.factory.createSmallSat
				val com = wrapper.factory.createXCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.allX.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			],
			[|
				val sat = wrapper.factory.createSmallSat
				val com = wrapper.factory.createKaCommSubsystem
				sat.commSubsystem.add(com)
				val selected = wrapper.allKa.select2(wrapper.rnd)
				com.target = selected.get(0)
				com.fallback = selected.get(1)
				wrapper.mission.spacecraft.add(sat)
				return true
			]
		]
		return options.get(wrapper.rnd.nextInt(options.size)).apply
	}
	def boolean removeSatellite(SatelliteModelWrapper wrapper){
		System.out.print("removeSatellite")
		val sat = wrapper.mission.spacecraft.get(wrapper.rnd.nextInt(wrapper.mission.spacecraft.size))
		val alternatives = sat.commSubsystem.map[com | com -> wrapper.linkableTo(com).toList]
		val incomings = sat.commSubsystem.map[com | com -> wrapper.incomingDirect(com)]
		  
		val precondition = alternatives.fold(true, [value, alt | value &&
			//every incoming link (if any) can be redirected without creating a loop
			(wrapper.incomingDirect(alt.key).toList.isEmpty || (!alt.value.isEmpty))
		])
		alternatives.fold(precondition, [value, alt | 
			// there are at least two alternatives
			alt.value.size>=2
		])
		if(!precondition){
			return false
		}
		for(incoming: incomings){
			val trg = incoming.key
			for(src : incoming.value.toList){
				if(src.target == trg){
					val alts = newArrayList
					alts.addAll(alternatives.findFirst[it.key.equals(trg)].value)
					if(!wrapper.gsn.commSubsystem.contains(src.fallback)){
						alts.remove(src.fallback)
					}
					src.target = alts.get(wrapper.rnd.nextInt(alts.size))
				}
				if(src.fallback == trg){
					val alts = newArrayList
					alts.addAll(alternatives.findFirst[it.key.equals(trg)].value)
					if(!wrapper.gsn.commSubsystem.contains(src.target)){
						alts.remove(src.target)
					}
					src.target = alts.get(wrapper.rnd.nextInt(alts.size))
				}
			} 
		}
		wrapper.mission.spacecraft.remove(sat)
		return true
	}
	def boolean addExtraComm(SatelliteModelWrapper wrapper){
		System.out.print("addComm")
		val nored = wrapper.mission.spacecraft.filter[it.commSubsystem.size<2]
		if(nored.isEmpty){
			return false
		}
		val sat = nored.get(wrapper.rnd.nextInt(nored.size))
		switch sat {
			case sat instanceof CubeSat3U : {
				if(wrapper.rnd.nextBoolean){
					sat.commSubsystem.add(wrapper.factory.createUHFCommSubsystem)
				} else {
					sat.commSubsystem.add(wrapper.factory.createXCommSubsystem)
				}
			}
			case sat instanceof CubeSat6U : {
				if(wrapper.rnd.nextBoolean){
					sat.commSubsystem.add(wrapper.factory.createUHFCommSubsystem)
				} else {
					sat.commSubsystem.add(wrapper.factory.createXCommSubsystem)
				}
			}
			case sat instanceof SmallSat : {
				switch wrapper.rnd.nextInt(3) {
					case 0:{
						sat.commSubsystem.add(wrapper.factory.createUHFCommSubsystem)
					}
					case 1:{
						sat.commSubsystem.add(wrapper.factory.createXCommSubsystem)
					}
					case 2:{
						sat.commSubsystem.add(wrapper.factory.createKaCommSubsystem)
					}
					
				}
			}
		}
		return true
	}
	def boolean removeExtraComm(SatelliteModelWrapper wrapper){
		System.out.print("removeComm")
		val twocom = wrapper.mission.spacecraft.filter[it.commSubsystem.size==2]
		val comms = newArrayList
		twocom.forEach[sat | 
			for(com : sat.commSubsystem){
				val in = wrapper.incomingDirect(com).toList
				val linkable = wrapper.linkableTo(com).toList
				if(in.isEmpty || linkable.size>=2){
					comms.add(sat->com)
				}
			}
		]
		
		if(comms.isEmpty){
			return false
		}
		val pair = comms.get(wrapper.rnd.nextInt(twocom.size))
		
		val alternative = wrapper.linkableTo(pair.value).toList
		val incomings = wrapper.incomingDirect(pair.value).toList
		val trg = pair.value
		
		for(src: incomings){
			if(src.target == trg){
				val alts = newArrayList
				alts.addAll(alternative)
				if(!wrapper.gsn.commSubsystem.contains(src.fallback)){
					alts.remove(src.fallback)
				}
				src.target = alts.get(wrapper.rnd.nextInt(alts.size))
			}
			if(src.fallback == trg){
				val alts = newArrayList
				alts.addAll(alternative)
				if(!wrapper.gsn.commSubsystem.contains(src.target)){
					alts.remove(src.target)
				}
				src.target = alts.get(wrapper.rnd.nextInt(alts.size))				}
		}
		return true
	}
	
	def boolean rerouteLink(SatelliteModelWrapper wrapper){
		System.out.print("rerouteLink")
		val links = wrapper.allLinks.toList
		if(links.isEmpty){println("Fail on no links")
			return false
		}
		
		val link = links.get(wrapper.rnd.nextInt(links.size))
		val alt = wrapper.linkableTo(link.key).filter([it!==link.value]).toList
		if(alt.isEmpty){
			println("Fail on no alternatives")
			return false
		}
		val to = alt.get(wrapper.rnd.nextInt(alt.size))
		
		if(link.key.target===link.value){
			link.key.target = to
		}
		if(link.key.fallback===link.value){
			link.key.fallback = to
		}
		return true
	}
}














