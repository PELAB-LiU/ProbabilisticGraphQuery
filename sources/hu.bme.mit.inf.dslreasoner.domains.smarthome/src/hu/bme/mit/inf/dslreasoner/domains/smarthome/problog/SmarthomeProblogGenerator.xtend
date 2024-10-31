package hu.bme.mit.inf.dslreasoner.domains.smarthome.problog

import smarthome.Smarthome
import org.eclipse.emf.ecore.EObject
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration
import smarthome.Measurement
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeHelper
import java.util.concurrent.TimeoutException

class SmarthomeProblogGenerator {
	val incbe = "incrementBE"
	val inc = "increment"
	val incb = "incrementbase"
	val in5m = "within5m"
	val aft = "after"
	val in5s = "within5s"
	val prs = "person"
	val nby = "nododyhome"
	val co = "highco"
	
	def generateFrom(Smarthome model){
		
		return '''
		«incrementevent(model)»
		0.925::«incb».
		«inc»(A,B) :- «incbe»(A,B),«incb».
		
		«in5min(model)»
		«after(model)»
		0.925::warningbase.
		tempwarning(D) :- after4(_,_,_,D),warningbase.
		after4(A,B,C,D) :- after3(A,B,C),after2(C,D),«in5m»(A,D).
		after3(A,B,C) :- after2(A,B),after2(B,C),«in5m»(A,C).
		after2(A,B) :- «inc»(_,A),«inc»(_,B),A\=B,«in5m»(A,B),«aft»(A,B).
		
		«highco(model)»
		«in5sec(model)»
		
		firewarning(M) :- «co»(M),tempwarning(D),«in5s»(M,D).
		
		«noperson(model)»
		«nododyhome(model)»
		
		callevent(M) :- firewarning(M),«nby»(M).
		callprobability(M,P) :- subquery(callevent(M),P).
		
		query(callprobability(M,P)).
		'''
	}
	def incrementevent(Smarthome model)'''
	«FOR home : model.homes»
	«FOR early : home.measurements»
	«FOR late : home.measurements»
	«IF incrementable(early,late)»
	«incrementProbability(early,late)»::«incbe.between(early,late)».
	«ENDIF»
	«ENDFOR»
	«ENDFOR»
	«ENDFOR»
	'''
	def in5min(Smarthome model)'''
	«FOR home : model.homes»
	«FOR early : home.measurements»
	«FOR late : home.measurements.filter[m | within5m(early,m)]»
	«in5m.between(early,late)».
	«ENDFOR»
	«ENDFOR»
	«ENDFOR»
	'''
	def after(Smarthome model)'''
	«FOR home : model.homes»
	«FOR early : home.measurements»
	«FOR late : home.measurements.filter[m | after(early,m)]»
	«aft.between(early,late)».
	«ENDFOR»
	«ENDFOR»
	«ENDFOR»
	'''
	def in5sec(Smarthome model)'''
	«FOR home : model.homes»
	«FOR early : home.measurements»
	«FOR late : home.measurements.filter[m | within5s(early,m)]»
	«in5s.between(early,late)».
	«ENDFOR»
	«ENDFOR»
	«ENDFOR»
	'''
	def noperson(Smarthome model)'''
	«FOR person : model.persons»
	«1-person.confidence»::«prs.between(person)».
	«ENDFOR»
	'''
	def nododyhome(Smarthome model)'''
	«FOR home : model.homes»
	«FOR m1 : home.measurements»
	«IF m1.athome.empty»
	1::«nby.between(m1)».
	«ELSE»
	«nby.between(m1)» :- «FOR p : m1.athome SEPARATOR ','»«prs.between(p)»«ENDFOR».
	«ENDIF»
	«ENDFOR»
	«ENDFOR»
	'''
	def highco(Smarthome model)'''
	«FOR home : model.homes»
	«FOR m1 : home.measurements.filter[m | m.co5k]»
	«coProbability(m1)»::«co.between(m1)».
	«ENDFOR»
	«ENDFOR»
	'''
	
	def Boolean incrementable(Measurement m1, Measurement m2){
		return SmarthomeHelper.incrementable(m1.temp, m1.time,m2.temp,m2.time)
	}
	def Double incrementProbability(Measurement m1, Measurement m2){
		return SmarthomeHelper.incrementConfidence(m1.temp,m1.time,m2.temp,m2.time)
	}
	def Boolean within5m(Measurement m1, Measurement m2){
		return SmarthomeHelper.within5m(m1.time,m2.time)
	}
	def Boolean after(Measurement m1, Measurement m2){
		return SmarthomeHelper.after(m1.time,m2.time)
	}
	def Boolean within5s(Measurement m1, Measurement m2){
		return SmarthomeHelper.within5s(m1.time,m2.time)
	}
	def between(String type, EObject... args) {
		ProbLogGeneration.dfact(type,args.map[arg|arg.name])
	}
	def Double coProbability(Measurement m1){
		return SmarthomeHelper.gt5000Confidence(m1.co)
	}
	def Boolean co5k(Measurement m1){
		return SmarthomeHelper.gt5000(m1.co)
	}
	
	def String getName(EObject object){
		return ""+object.hashCode()
	}
}