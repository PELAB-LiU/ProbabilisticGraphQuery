package se.liu.ida.sas.pelab.satellite1.storm

import java.util.List
import se.liu.ida.sas.pelab.storm.transformation.StormGeneration
import org.eclipse.emf.ecore.EObject
import satellite1.InterferometryMission

class StormSatelliteGenerator {
	def Pair<String,List<String>> generateFrom(InterferometryMission model){
		/*// increments -> storm
		val tempBE = null
		
		val stormmodel = '''
		
		'''
		
		val tops = calls.key.map[StormGeneration.topEvent(key(callevent, it))].toList
		*/return null//stormmodel -> tops
	}
	
	def key(String name, EObject... args){
		return '''«name»_«FOR arg : args SEPARATOR "_"»«arg.name»«ENDFOR»'''
	}
	def String getName(EObject object){
		return '''«object.class.simpleName»«object.hashCode»'''
	}
}