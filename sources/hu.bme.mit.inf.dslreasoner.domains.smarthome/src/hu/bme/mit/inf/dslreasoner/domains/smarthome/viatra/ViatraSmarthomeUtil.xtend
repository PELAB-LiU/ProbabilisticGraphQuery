package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra

import hu.bme.mit.inf.measurement.utilities.viatra.EngineConfig
import java.util.Map
import org.eclipse.emf.ecore.EObject

interface ViatraSmarthomeUtil {
	def String getMatchesJSON(EngineConfig engine, Map<EObject,String> index){
		val opt = engine.parsed.getQuerySpecification("callProbability")
		if(opt.isPresent){
			val matcher = opt.get
			'''
			{
				"valid" : true,
				"matches" : [
				«FOR match : engine.engine.getMatcher(matcher).allMatches SEPARATOR ","»
					{
						"measurement" : "«index.get(match.get(0))»",
						"probability" : «match.get(1)»
					}
				«ENDFOR»
				]
			}
			'''
		} else {
			return '''{"valid" : false, "matches" : []}'''
		}
	}
}