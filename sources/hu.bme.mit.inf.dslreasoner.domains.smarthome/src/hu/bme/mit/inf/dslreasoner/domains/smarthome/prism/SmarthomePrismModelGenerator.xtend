package hu.bme.mit.inf.dslreasoner.domains.smarthome.prism

import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine
import org.eclipse.emf.ecore.EObject
import java.util.Map

@Deprecated
class SmarthomePrismModelGenerator {
	def generatePrismModel(Map<EObject, String> names, PatternParsingResults parsed, ViatraQueryEngine engine){
		val variables = '''
		module variables
			«FOR match : getMatches("prism_tempIncrBE", parsed, engine)»
			«names.map(match.get(0))» : bool init true;
			«ENDFOR»
			TI : bool init true;
			TW : bool init true;
			
			
			«FOR match : getMatches("prism_tempIncrBE", parsed, engine)»
			[] «names.map(match.get(0))» -> «rateOf(match.get(1) as Double)» : («names.map(match.get(0))»'=false);
			«ENDFOR»
			[] TI -> «rateOf(0.925)» : (TI'=false);
			[] TW -> «rateOf(0.925)» : (TW'=false);
		endmodule
		'''
		
		val incrementevents = '''
		//Matches for temperature increments
		«FOR match : getMatches("prism_tempIncrBE", parsed, engine)»
		formula incr_«names.map(match.get(0))» = «names.map(match.get(0))» & TI;
		«ENDFOR»
		
		//Matches for temperature warnings with many arg
		«FOR match : getMatches("fourWarning",parsed,engine)»
		formula warn4_«names.map(match.get(1))»_«names.map(match.get(2))»_«names.map(match.get(3))»_«names.map(match.get(4))» = «names.map(match.get(1))» & «names.map(match.get(2))» & «names.map(match.get(3))» & «names.map(match.get(4))» & TW;
		«ENDFOR»
		//Aggregate temperature warnings
		«FOR measurement : unique("fourWarning",4,parsed,engine)»
		formula warn_«names.map(measurement)» = TW & 
		«FOR match : getPartialMatches("fourWarning", 4, measurement,parsed,engine) SEPARATOR ' & '»
			warn4_«names.map(match.get(1))»_«names.map(match.get(2))»_«names.map(match.get(3))»_«names.map(match.get(4))»
		«ENDFOR»
		;
		«ENDFOR»
		
		
		
		'''
		return '''
		ctmc
		
		«variables»
		
		«incrementevents»
		'''
	}
	
	def String map(Map<EObject, String> map, Object object){
		return map.get(object);
	}
	
	def double rateOf(double p){
		return - Math.log(1-p)
	}
	def getMatches(String name, PatternParsingResults parsed, ViatraQueryEngine engine){
		val matches = newArrayList
		parsed.getQuerySpecification(name).ifPresent([specification|
			val matcher = engine.getMatcher(specification)
			matches.addAll(matcher.allMatches)
		])
		return matches
	}
	def unique(String name, int index, PatternParsingResults parsed, ViatraQueryEngine engine){
		val matches = newHashSet
		parsed.getQuerySpecification(name).ifPresent([specification|
			val matcher = engine.getMatcher(specification)
			matcher.forEachMatch([match | 
				matches.add(match.get(index))
			])
		])
		return matches
	}
	def getPartialMatches(String name, int index, Object object, PatternParsingResults parsed, ViatraQueryEngine engine){
		val matches = newArrayList
		parsed.getQuerySpecification(name).ifPresent([specification|
			val matcher = engine.getMatcher(specification)
			matcher.forEachMatch([match | 
				if(match.get(index).equals(object)){
					matches.add(match)
				}
			])
		])
		return matches
	}
}