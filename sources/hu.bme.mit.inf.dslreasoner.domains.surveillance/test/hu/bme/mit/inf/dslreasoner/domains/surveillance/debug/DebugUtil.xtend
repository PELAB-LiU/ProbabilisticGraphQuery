package hu.bme.mit.inf.dslreasoner.domains.surveillance.debug

import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import uncertaindatatypes.UReal
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig

interface DebugUtil {
	def String ureal(UReal ureal){
		return '''
		{
			"x":«ureal.x»,
			"u":«ureal.u»
		}
		'''
	}
	def String coordinate(Coordinate c){
		return '''
		{
			"x": «ureal(c.x)»,
			"y": «ureal(c.y)»
		}
		'''
	}
	def String comaprtison(Coordinate a, Coordinate b, boolean result){
		return '''
		{
			"a": «coordinate(a)»,
			"b": «coordinate(b)»,
			"r":«result»
		}
		'''
	}
	def String probability(Coordinate from, Coordinate to, UReal speed, double confidence, double result){
		return '''
		{
			"from": «coordinate(from)»,
			"to": «coordinate(to)»,
			"speed": «ureal(speed)»,
			"confidence": «confidence»,
			"r":«result»
		}
		'''
	}
	def String getMatchesJSON(EngineConfig engine, Map<EObject,Integer> index){
		return '''
			{
				«FOR IQuerySpecification spec : engine.parsed.querySpecifications SEPARATOR ","»
				"«spec.fullyQualifiedName»" : {
					"count": «engine.engine.getMatcher(spec).countMatches»,
					"matches": [
						«FOR match : engine.engine.getMatcher(spec).allMatches SEPARATOR ","»
						{
							«FOR arg : spec.parameterNames SEPARATOR ","»
							«IF match.get(arg.toString) instanceof EObject»
							"«arg»": «index.get(match.get(arg.toString))?: '''"«match.get(arg.toString)»"'''»
							«ELSEIF match.get(arg.toString) instanceof Number »
							"«arg»": «match.get(arg.toString)»
							«ELSE»
							"«arg»": "«match.get(arg.toString)»"
							«ENDIF»
							«ENDFOR»
						}
						«ENDFOR»
					]
				}
				«ENDFOR»
			}
		'''
	}
}
