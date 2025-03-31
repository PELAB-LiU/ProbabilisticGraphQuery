package se.liu.ida.sas.pelab.smarthome.storm

import smarthome.Smarthome
import java.util.List
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration
import smarthome.Measurement
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeHelper
import org.eclipse.emf.ecore.EObject
import hu.bme.mit.inf.measurement.utilities.Quad
import java.util.Set

class StormSmarthomeGenerator {
	static val warningbase = "warningbase"
	static val incrementbase = "incrementbase"
	static val inc2 = "increment_2"
	static val tempInc1 = "tempincrement"
	static val warning4 = "warning4"
	static val cobe = "cobe"
	static val fireevent = "fire"
	static val certain = "certain"
	static val personevent = "person"
	static val nobodyhome = "nbhome"
	static val callevent = "callevent"
	
	def Pair<String,List<String>> generateFrom(Smarthome model){
		// increments -> storm
		val tempBE = tempIncrBE(model)
		val temp = temp(tempBE.key)
		val warning4 = fourWarinings(model, temp.key)
		val co = highCO(model)
		val fire = fire(co.key, warning4.key)
		val person = personBE(model)
		val nbhome = nobodyHome(model)
		val calls = call(fire.key)
		
		val stormmodel = '''
		«tempBE.value»
		«temp.value»
		«warning4.value»
		«co.value»
		«fire.value»
		«person.value»
		«nbhome.value»
		«calls.value»
		«StormGeneration.basicEvent(warningbase,0.925)»
		«StormGeneration.basicEvent(incrementbase,0.925)»
		«StormGeneration.basicEvent(certain,1)»
		'''
		
		val tops = calls.key.map[StormGeneration.topEvent(key(callevent, it))].toList
		return stormmodel -> tops
	}
	def call(Set<Measurement> fires){
		return fires -> '''
		«FOR fire : fires»
		«StormGeneration.andGate(key(callevent, fire),
			key(fireevent, fire),
			key(nobodyhome, fire)
		)»
		«ENDFOR»
		'''
	}
	def nobodyHome(Smarthome model){
		val nbhome = newArrayList
		model.persons
		for(home : model.homes){
			nbhome.addAll(home.measurements)
		}
		return nbhome -> '''
		«FOR m : nbhome»
		«IF m.athome.isEmpty»
		«StormGeneration.andGate(key(nobodyhome,m),
			certain
		)»
		«ELSE»
		«StormGeneration.andGate(key(nobodyhome,m),
			m.athome.map[p | key(personevent, p)]
		)»
		«ENDIF»
		«ENDFOR»
		'''
	}
	//negation is not supported thus the basic event is negated.
	def personBE(Smarthome model){
		return model.persons -> '''
		«FOR person : model.persons»
		«StormGeneration.basicEvent(key(personevent, person), 1-person.confidence)»
		«ENDFOR»
		'''
	}
	def fire(List<Measurement> cos, Set<Measurement> warnings){
		val fires = newArrayList
		for(co : cos){
			val warn = warnings
					.filter[w | w.eContainer===co.eContainer]//same home
					.filter[w | SmarthomeHelper.within5s(co.time, w.time)]
			warn.forEach[w | fires.add(co  -> w)]
		}
		val helperName = fireevent+"helper"
		return fires.map[it.key].toSet -> '''
		«FOR fire : fires.map[it.key].toSet.map[co | co -> fires.filter[it.key===co].map[it.value]]»
		«StormGeneration.orGate(key(helperName, fire.key),
			fire.value.map[key(warning4, it)]
		)»
		«StormGeneration.andGate(key(fireevent,fire.key), 
			key(cobe, fire.key),
			key(helperName,fire.key)
		)»
		«ENDFOR»
		'''
	}
	def highCO(Smarthome model){
		val highCO = newArrayList
		for(home : model.homes){
			highCO.addAll(home.measurements.filter[m | SmarthomeHelper::gt5000(m.co)])
		}
		return highCO -> '''
		«FOR m : highCO»
		«StormGeneration.basicEvent(key(cobe, m), SmarthomeHelper.gt5000Confidence(m.co))»
		«ENDFOR»
		'''
	}
	
	def fourWarinings(Smarthome model, Set<Measurement> increments){
		val warnings4 = newArrayList
		for(home : model.homes){
			val measurements1 = home.measurements.filter[increments.contains(it)]
			for(m1 : measurements1){
				val measurements2 = measurements1.filter[m2 |
					SmarthomeHelper.after(m1.time, m2.time) &&
					SmarthomeHelper.within5m(m1.time, m2.time)
				]
				for(m2 : measurements2){
					val measurements3 = measurements2.filter[m3|
						SmarthomeHelper.after(m2.time, m3.time) &&
						SmarthomeHelper.within5m(m1.time, m3.time)
					]
					for(m3 : measurements3){
						val measurements4 = measurements3.filter[m4 |
							SmarthomeHelper.after(m3.time, m4.time) &&
							SmarthomeHelper.within5m(m1.time, m4.time)
						]
						for(m4 : measurements4){
							warnings4.add(new Quad(m1,m2,m3,m4))
						}
					}
				}
			} 
		}
		val helperName = warning4+"helper"
		return warnings4.map[it.forth].toSet -> '''
		«FOR m4 : warnings4.map[it.forth].toSet»
		«
			{val tuples = warnings4.filter[it.forth===m4]
					//.map[quad | key(helperName, quad.fisrt, quad.second, quad.third, quad.forth)]
					
			'''
			«FOR tuple : tuples»
			«StormGeneration.andGate(key(helperName, tuple.fisrt, tuple.second, tuple.third, tuple.forth),
							incrementbase,
							key(tempInc1, tuple.fisrt),
							key(tempInc1, tuple.second),
							key(tempInc1, tuple.third),
							key(tempInc1, tuple.forth)
						)»
			«ENDFOR»
			«StormGeneration.orGate(key(warning4, m4),
				tuples.map[quad | key(helperName, quad.fisrt, quad.second, quad.third, quad.forth)]
			)»
			'''
			}
		»
		«ENDFOR»
		'''
	}
	def temp(List<Pair<Measurement,Measurement>> increments){
		val tempInc = increments.map[pair | pair.value].toSet
		val helperName = tempInc1+"Helper"
		
		return tempInc -> '''
		«FOR inc : tempInc»
		«
			{val incrementEvents = increments.filter[entry | entry.value===inc]
					.map[increment | key(inc2, increment.key, increment.value)]
			'''
			«StormGeneration.andGate(key(tempInc1,inc),
				incrementbase,
				key(helperName, inc)
			)»
			«StormGeneration.orGate(key(helperName, inc),
				incrementEvents
			)»
			'''
			}
		»
		«ENDFOR»
		'''
	}
	def tempIncrBE(Smarthome model){
		val increments = newArrayList 
		for(home: model.homes){
			for(measurement1 : home.measurements){
				for(measurement2 : home.measurements){
					if(incrementable(measurement1, measurement2)){
						increments.add(measurement1->measurement2)
					}
				}
			}
		}
		return increments -> '''
		«FOR increment :  increments»
		«StormGeneration.basicEvent(
			key(inc2, increment.key, increment.value), 
			incrementProbability(increment.key,increment.value)
		)»
		«ENDFOR»
		'''
	}
	def Double incrementProbability(Measurement m1, Measurement m2){
		return SmarthomeHelper.incrementConfidence(m1.temp,m1.time,m2.temp,m2.time)
	}
	def Boolean incrementable(Measurement m1, Measurement m2){
		return SmarthomeHelper.incrementable(m1.temp, m1.time,m2.temp,m2.time)
	}
	def key(String name, EObject... args){
		return '''«name»_«FOR arg : args SEPARATOR "_"»«arg.name»«ENDFOR»'''
	}
	def String getName(EObject object){
		return '''«object.class.simpleName»«object.hashCode»'''
	}
}