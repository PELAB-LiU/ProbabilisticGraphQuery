package se.liu.ida.sas.pelab.surveillance.storm

import surveillance.SurveillanceModel
import surveillance.Drone
import surveillance.UnidentifiedObject
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper
import org.eclipse.emf.ecore.EObject
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration
import java.util.List

class StormSurveillanceGenerator {
	val gs = "gunshot"
	val trg = "targettable"
	val att = "attempt"
	val elim = "elimination"
	
	def generateFrom(SurveillanceModel model){
		val targettableUFOs = model.ufos.filter[obj|targettable(obj)]
		val gunshots = newArrayList
		for(drone : model.drones){
			for(ufo : model.ufos.filter[ufo | shootable(drone, ufo)]){
				gunshots.add(drone -> ufo)
			}
		}
		val attempts = newArrayList
		for(to : targettableUFOs){
			val froms = gunshots.filter[pair | to===pair.value].map[pair | pair.key]
			for(from : froms){
				attempts.add(from -> to)
			}
		}
		val elimmap = newHashMap
		for(attempt : attempts){
			elimmap.putIfAbsent(attempt.value, newArrayList)
			val list = elimmap.get(attempt.value)
			list.add(attempt.key)
		}
		val eliminations = elimmap.entrySet.map[entry | entry.key -> (entry.value as List<Drone>)]
		
		
		val base = '''
		«complexElimination(eliminations)»
		«complexAttempt(attempts)»
		«basicEventTargettable(targettableUFOs)»
		«basicEventGunshots(gunshots)»
		'''
		val tops = eliminations.map[entry | StormGeneration.topEvent(key(elim,entry.key))].toList
		return base -> tops
	}
	def complexElimination(Iterable<Pair<UnidentifiedObject,List<Drone>>> eliminations){
		return '''
		«FOR elimination :  eliminations»
		«StormGeneration.orGate(key(elim, elimination.key),
			elimination.value.map[drone | key(att,drone,elimination.key)]
		)»
		«ENDFOR»
		'''
	}
	def complexAttempt(List<Pair<Drone,UnidentifiedObject>> attempts){
		return '''
		«FOR attempt : attempts»
		«StormGeneration.andGate(key(att, attempt.key, attempt.value), 
			key(gs, attempt.key, attempt.value),
			key(trg, attempt.value)
		)»
		«ENDFOR»
		'''
	}
	def basicEventGunshots(List<Pair<Drone,UnidentifiedObject>> shots){
		return '''
		«FOR shot : shots»
		«StormGeneration.basicEvent(
			key(gs, shot.key, shot.value), 
			SurveillanceHelper.shotProbability(shot.key.position,shot.value.position,shot.value.speed,shot.value.confidence))»
		«ENDFOR»
		'''
	}
	def basicEventTargettable(Iterable<UnidentifiedObject> ufos){
		return '''
		«FOR ufo : ufos»
		«StormGeneration.basicEvent(
			key(trg, ufo), 
			ufo.confidence
		)»
		«ENDFOR»
		'''
	}
	def drones(SurveillanceModel model){
		return model.objects
			.filter[obj | obj instanceof Drone]
			.map[obj | obj as Drone]
	}
	def ufos(SurveillanceModel model){
		return model.objects
			.filter[obj | obj instanceof UnidentifiedObject]
			.map[obj | obj as UnidentifiedObject]
	}
	def boolean targettable(UnidentifiedObject trg){
		return trg.confidence>0.65 
			&& SurveillanceHelper.spd30(trg.speed)
	}
	def boolean shootable(Drone from, UnidentifiedObject to){
		return SurveillanceHelper.dst1000(from.position,to.position)
	}
	def key(String name, EObject... args){
		return '''«name»_«FOR arg : args SEPARATOR "_"»«arg.name»«ENDFOR»'''
	}
	
	def String getName(EObject object){
		return '''«object.class.simpleName»«object.hashCode»'''
	}
}




