package hu.bme.mit.inf.dslreasoner.domains.surveillance.problog

import surveillance.SurveillanceModel
import surveillance.UnidentifiedObject
import surveillance.Drone
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration
import org.eclipse.emf.ecore.EObject
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper

class ProblogSurveillanceGenerator {
	val gs = "gunshot"
	val trg = "targettable"
	def generateFrom(SurveillanceModel model){
		
		'''
		«FOR ufo : model.ufos.filter[obj|targettable(obj)]»
		«objectevent(ufo)»
		«ENDFOR»
		«FOR drone : model.drones»
		«FOR obj : model.ufos.filter[ufo | shootable(drone, ufo)]»
		«gsevent(drone, obj)»
		«ENDFOR»
		«ENDFOR»
		
		attempt(D,O) :- «gs»(D,O),«trg»(O).
		elimination(O) :- attempt(_,O).
		
		result(O,R) :- subquery(elimination(O),R).
		query(result(O,R)).
		'''
	}
	def gsevent(Drone from, UnidentifiedObject to)'''
	«SurveillanceHelper
		.shotProbability(from.position,to.position,to.speed,to.confidence)»::«gs.between(from,to)».
	'''
	def objectevent(UnidentifiedObject obj)'''
	«obj.confidence»::«trg.between(obj)».
	'''
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
	
	def between(String type, EObject... args) {
		ProbLogGeneration.dfact(type,args.map[arg|arg.name])
	}
	
	def String getName(EObject object){
		return ""+object.hashCode()
	}
}