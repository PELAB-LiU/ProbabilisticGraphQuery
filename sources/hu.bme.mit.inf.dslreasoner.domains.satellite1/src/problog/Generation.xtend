package problog

import satellite1.InterferometryMission
import satellite1.CubeSat3U
import satellite1.CubeSat6U
import satellite1.SmallSat
import satellite1.KaCommSubsystem
import satellite1.UHFCommSubsystem
import satellite1.XCommSubsystem
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration
import satellite1.Spacecraft
import org.eclipse.emf.ecore.EObject
import satellite1.CommSubsystem
import hu.bme.mit.inf.dslreasoner.domains.satellite1.performability.Performability

class Generation {
	val op = "operational"
	val ss = "subsystem"
	val lnk = "link"
	val pld = "payload"
	val rdy = "ready"
	val on = "online"
	
	def generateFrom(InterferometryMission mission)'''
	:- use_module(library(aggregate)).
	
	«FOR satellite : mission.spacecraft»
	«ProbLogGeneration.basicEvent(satellite.probability,op,satellite.name)»
	«IF satellite.payload!==null»«pld.between(satellite)» :- «on.between(satellite)».«ENDIF»
	«FOR subsystem : satellite.commSubsystem»
	«ProbLogGeneration.basicEvent(subsystem.probability,op,subsystem.name)»
	«ss.between(satellite,subsystem)».
	«IF subsystem.target!==null»«lnk.between(subsystem,subsystem.target)».«ENDIF»
	«IF subsystem.fallback!==null»«lnk.between(subsystem,subsystem.fallback)».«ENDIF»
	«ENDFOR»
	«ENDFOR»
	
	«FOR gsc : mission.groundStationNetwork.commSubsystem»
	«rdy.between(gsc)».
	«ENDFOR»
	
	«FOR size : 2..mission.spacecraft
		.filter([sat|sat.payload!==null])
		.size»
	utility(«size»,«Performability.calculate2(size)»).
	«ENDFOR»
	
	«on»(Sat) :- «ss»(Sat,Comm),«lnk»(Comm,Other),«op»(Sat),«op»(Comm),«rdy»(Other).
	«rdy»(Comm) :- «ss»(Sat,Comm),«op»(Comm),«on»(Sat).
	
	member(X,[X|R]).
	member(X,[H|R]) :- X\=H,member(X,R).
	
	unique([],0).
	unique([X|R],S) :- member(X,R),unique(R,S).
	unique([X|R],S) :- \+ member(X,R),unique(R,S1),S is S1 + 1.
	
	%available(Size) :- findall(Sat,«pld»(Sat),Payloads),unique(Payloads,Size),Size >= 2.
	available(length<P>) :- «pld»(P).
	coverage(Size,Reward) :- subquery(available(Size),Prob),utility(Size,Utility),Reward is Prob * Utility.
	expected(sum<Reward>) :- coverage(_,Reward).
	
	query(expected(S)).
	
	'''
	
	def between(String type, EObject... args) {
		ProbLogGeneration.dfact(type,args.map[arg|arg.name])
	}
	
	def String getName(EObject object){
		return ""+object.hashCode()
	}
	
	def dispatch probability(CubeSat3U satellite){0.98400034407713}
	def dispatch probability(CubeSat6U satellite){0.98496269152523}
	def dispatch probability(SmallSat satellite){0.98581584235241}
	def dispatch probability(KaCommSubsystem subsystem){0.90483741803596}
	def dispatch probability(UHFCommSubsystem subsystem){0.92004441462932}
	def dispatch probability(XCommSubsystem subsystem){0.92596107864232}
}