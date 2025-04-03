package se.liu.ida.sas.pelab.satellite1.storm

import java.util.List
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration
import org.eclipse.emf.ecore.EObject
import satellite1.InterferometryMission
import satellite1.CubeSat3U
import satellite1.CubeSat6U
import satellite1.SmallSat
import satellite1.KaCommSubsystem
import satellite1.UHFCommSubsystem
import satellite1.XCommSubsystem

class StormSatelliteGenerator {
	static val operational = "op"
	static val ready = "rdy"
	static val online = "on"
	static val available = "av"
	static val coverage = "cov"
	
	def Pair<String,List<String>> generateFrom(InterferometryMission model){
		val stormmodel = '''
		«operational(model)»
		«ready(model)»
		«online(model)»
		«payload(model)»
		«coverage(model)»
		'''
		
		val tops = (2..model.spacecraft.map[it.payload].filter[it!==null].size).map[
			StormGeneration.topEvent(key(coverage+it))
		].toList
		return stormmodel -> tops
	}
	def coverage(InterferometryMission mission){
		val payloads = mission.spacecraft.map[it.payload].filter[it!==null]
		for(count : 2..payloads.size){
			
		}
		return '''
		«FOR cnt : 2..payloads.size»
		«StormGeneration.kof(cnt, key(coverage+cnt),
			payloads.map[key(available, it)]
		)»
		«ENDFOR»
		'''
	}
	
	def payload(InterferometryMission mission){
		val payloads = mission.spacecraft.map[it.payload].filter[it!==null]
		
		return '''
		«FOR pld : payloads»
		«StormGeneration.andGate(key(available, pld),
			key(online, pld.eContainer)
		)»
		«ENDFOR»
		'''
	}
	
	def online(InterferometryMission mission){
		val onlineHelper = online+"LinkHelper"
		val transmittingHelper = online+"TransmitHelper"
		val linkHelper = online+"LinkHelper"
		
		'''
		«FOR sat : mission.spacecraft»
		«{
			val transmittingList = sat.commSubsystem.filter[it.target!==null || it.fallback!==null]
			'''
			«FOR transmitting : transmittingList»
			«StormGeneration.orGate(key(linkHelper, transmitting),
				key(ready, transmitting.target),
				key(ready, transmitting.fallback)
			)»
			«StormGeneration.andGate(key(transmittingHelper, transmitting),
				key(operational, transmitting),
				key(linkHelper, transmitting)
			)»
			«ENDFOR»
			«StormGeneration.orGate(key(onlineHelper, sat),
				transmittingList.map[key(transmittingHelper, it)]
			)»
			'''
		}»
		«StormGeneration.andGate(key(online, sat),
			key(operational, sat),
			key(onlineHelper, sat)
		)»
		«ENDFOR»
		'''
	}
	def ready(InterferometryMission mission){
		return '''
		«FOR gcomm : mission.groundStationNetwork.commSubsystem»
		«StormGeneration.basicEvent(key(ready, gcomm), 1.0)»
		«ENDFOR»
		«FOR sat : mission.spacecraft»
		«FOR comm : sat.commSubsystem»
		«StormGeneration.andGate(key(ready, comm),
			key(online, sat),
			key(operational, comm)
		)»
		«ENDFOR»
		«ENDFOR»
		'''
	}
	def operational(InterferometryMission mission){
		val components = newArrayList
		mission.spacecraft.forEach[satellite |
			components.add(satellite)
			components.addAll(satellite.commSubsystem)
		]
		val events = '''
		«FOR comp : components»
		«StormGeneration.basicEvent(key(operational, comp), probability(comp))»
		«ENDFOR»
		'''
		components.addAll(mission.groundStationNetwork.commSubsystem)
		return events
	}
	def key(String name, EObject... args){
		return '''«name»_«FOR arg : args SEPARATOR "_"»«arg.name»«ENDFOR»'''
	}
	def String getName(EObject object){
		return '''«object.class.simpleName»«object.hashCode»'''
	}
	def dispatch probability(CubeSat3U satellite){0.98400034407713}
	def dispatch probability(CubeSat6U satellite){0.98496269152523}
	def dispatch probability(SmallSat satellite){0.98581584235241}
	def dispatch probability(KaCommSubsystem subsystem){0.90483741803596}
	def dispatch probability(UHFCommSubsystem subsystem){0.92004441462932}
	def dispatch probability(XCommSubsystem subsystem){0.92596107864232}
}