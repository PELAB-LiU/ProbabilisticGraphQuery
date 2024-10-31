package hu.bme.mit.inf.measurement

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.eclipse.emf.ecore.EPackage
import satellite1.SatellitePackage
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.xtext.diagnostics.Severity
import java.io.File
import reliability.mdd.MddModel
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import org.eclipse.emf.common.util.URI
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.emf.EMFScope
import satellite1.SatelliteFactory
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine
import org.junit.Test
import java.io.FileWriter
import problog.Generation
import satellite1.InterferometryMission
import java.util.HashMap
import java.util.Scanner

class SatelliteTest {
	@Test
	def void init(){
		Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.putIfAbsent("xmi", new XMIResourceFactoryImpl())
		StochasticPatternGenerator.doSetup
		val generator = new StochasticPatternGenerator
		
		EMFPatternLanguageStandaloneSetup.doSetup
		ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
			LocalSearchEMFBackendFactory.INSTANCE)
		
		EPackage.Registry.INSTANCE.put(SatellitePackage.eINSTANCE.nsURI, SatellitePackage.eINSTANCE)

		val transformed = generator.transformPatternFile(new File("../hu.bme.mit.inf.dslreasoner.domains.satellite1/src/hu/bme/mit/inf/dslreasoner/domains/satellite1/queries/reliability.vql"))
		val parsed = parseQueries(transformed)
		if(parsed.hasError){
			parsed.errors.forEach[println('''Parse error: «it»''')]
		}
		
		MddModel.changeTo("standalone")
		val standaloneMDD = MddModel.getInstanceOf("standalone")
		standaloneMDD.resetModel
		val standaloneResourceSet = new ResourceSetImpl
		standaloneResourceSet.resources.clear
		
		parsed.querySpecifications.forEach [ IQuerySpecification<? extends ViatraQueryMatcher> specification |
			standaloneMDD.registerSpecificationIfNeeded(specification)
		]
		
		val traceRes = standaloneResourceSet.createResource(URI.createFileURI("trace-tmp-std.xmi"))
		traceRes.contents.add(standaloneMDD.traceModel)
		
		val standalone = 
			AdvancedViatraQueryEngine.createUnmanagedEngine(new EMFScope(standaloneResourceSet))
			
		standaloneMDD.initializePatterns(standalone)
		#["coverage","ua_online"].forEach([name | 
			parsed.getQuerySpecification(name).ifPresent([IQuerySpecification<? extends ViatraQueryMatcher> specification |
				val cnt = standalone.getMatcher(specification).countMatches
			])
		])
		
		/**
		 * Build instance model
		 */
		val factory = SatelliteFactory.eINSTANCE
		val mission = factory.createInterferometryMission
		
		val g = factory.createGroundStationNetwork
		val cg = factory.createXCommSubsystem
		g.commSubsystem.add(cg)
		mission.groundStationNetwork = g
		
		val s1 = factory.createSmallSat
		val c1 = factory.createXCommSubsystem
		s1.commSubsystem.add(c1)
		c1.target = cg
		s1.payload = factory.createInterferometryPayload
		mission.spacecraft.add(s1)
		
		val s2 = factory.createSmallSat
		val c2 = factory.createXCommSubsystem
		s2.commSubsystem.add(c2)
		c2.target = c1
		s2.payload = factory.createInterferometryPayload
		mission.spacecraft.add(s2)
		
		val resource = standaloneResourceSet.createResource(URI.createFileURI("tmp-domain-standalone.xmi"))
		resource.contents.add(mission)
		
		standaloneMDD.unaryForAll(standalone)
		checkMatches("coverage", parsed, standalone)
		runProblog(mission)
		/*
		 * 
		val s3 = factory.createSmallSat
		val c3 = factory.createXCommSubsystem
		s3.commSubsystem.add(c3)
		c3.target = cg
		c2.fallback = c3
		mission.spacecraft.add(s3)
		//*/
		
		/*
		 * 
		 */
		val c3 = factory.createXCommSubsystem
		s1.commSubsystem.add(c3)
		c3.target = cg
		c2.fallback = c3
		//*/
		//c2.target = c1
		
		
		standaloneMDD.unaryForAll(standalone)
		checkMatches("coverage", parsed, standalone)
		runProblog(mission)
	}
	
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
	
	def checkMatches(String name, PatternParsingResults parsed, ViatraQueryEngine engine){
		val cnt = newDoubleArrayOfSize(1)
		parsed.getQuerySpecification(name).ifPresent([specification |
			val matcher = engine.getMatcher(specification)	
			println("Specification found: "+specification.simpleName)
			matcher.forEachMatch([match |
				println("\t"+match.prettyPrint)
			])
			/*matcher.oneArbitraryMatch.ifPresent([match |
				cnt.set(0, match.get(0) as Double)
			])*/
		])
		//return cnt.get(0)
	}
	
	def runProblog(InterferometryMission mission) {
		val file = new File("foo.pl")
		file.createNewFile
		val writer = new FileWriter(file)
		val builder = new ProcessBuilder("problog", "foo.pl");
		//var Process process = null

		val start = System.nanoTime
		val plmodel = (new Generation).generateFrom(mission).toString
		writer.write(plmodel)
		writer.flush
		writer.close
		val trafo = System.nanoTime
		val process = builder.start


		val output = new HashMap<String, Object>
		val io = new Scanner(process.inputStream)
		io.forEach [ line | println("Debug: " + line)]

	}
	
	
}