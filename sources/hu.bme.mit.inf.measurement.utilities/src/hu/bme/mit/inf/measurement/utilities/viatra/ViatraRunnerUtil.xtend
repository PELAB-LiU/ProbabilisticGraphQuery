package hu.bme.mit.inf.measurement.utilities.viatra

import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.viatra.query.runtime.api.IPatternMatch

interface ViatraRunnerUtil {
	def parseQueries(String vql){
		val result = PatternParserBuilder.instance.parse(vql)
		val fault = result.hasError
		result.allDiagnostics.forEach[issue | 
			if(issue.severity === Severity.ERROR){
				println("Error: "+issue)
			}
		]
		if(fault){
			System.err.println(vql)
		}
		return result
	}
	
	def int countBasicEvents(EngineConfig engine){
		val cnt = newIntArrayOfSize(2)
		engine.parsed.getQuerySpecification("BERequiredName1").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			cnt.set(0,matcher.countMatches)
		])
		engine.parsed.getQuerySpecification("BERequiredName2").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			cnt.set(1,matcher.countMatches)
		])
		return cnt.get(0)+cnt.get(1)
	}
	def countStochasticPatterns(EngineConfig engine){
		val cnt = newIntArrayOfSize(1)
		engine.parsed.getQuerySpecification("stochasticCount").ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)
			matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Integer)
			])
		])
		return cnt.get(0)
	}
	def checkMatches(String name, EngineConfig engine){
		val cnt = newDoubleArrayOfSize(1)
		engine.parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
			matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Double)
			])
		])
		return cnt.get(0)
	}
	def printMatches(String name, EngineConfig engine){
		engine.parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
		])
	}
	def initializePatterns(EngineConfig engine, String... queries){
		queries.forEach([name | 
			engine.parsed.getQuerySpecification(name).ifPresent([IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> specification |
				engine.engine.getMatcher(specification).countMatches
			])
		])
	}
}