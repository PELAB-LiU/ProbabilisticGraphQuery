package hu.bme.mit.inf.measurement.utilities.viatra

import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration
import org.eclipse.emf.ecore.EPackage
import hu.bme.mit.inf.querytransformation.query.StochasticPatternGenerator
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.rete.matcher.ReteBackendFactory
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchEMFBackendFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import hu.bme.mit.inf.measurement.utilities.CSVLog

abstract class BaseRunner<Config extends BaseConfiguration> {
	static val Logger LOG4J = LoggerFactory.getLogger(ScalingMeasurementRunner);
	
	protected val Config cfg
	
	protected val StochasticPatternGenerator generator
	protected val String transformed
	
	new(Config cfg, EPackage domain){
		this.cfg = cfg

		Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.putIfAbsent("xmi", new XMIResourceFactoryImpl())
		StochasticPatternGenerator.doSetup
		generator = new StochasticPatternGenerator
		
		EMFPatternLanguageStandaloneSetup.doSetup
		ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
			LocalSearchEMFBackendFactory.INSTANCE)
		
		EPackage.Registry.INSTANCE.put(domain.nsURI, domain)

		transformed = generator.transformPatternFile(cfg.vql)
		LOG4J.info("Queries {}", transformed)
	}
	
	def gc(){
		LOG4J.debug("GC {}ms", cfg.GCTime)
		System.gc()
		Thread.sleep(cfg.GCTime)
		System.gc()
		Thread.sleep(cfg.GCTime)
	}
	
	def void run(){
		val warmlog = new CSVLog(cfg.CSVcolumns,cfg.delimiter)
		try{
			warmup(warmlog)
		} finally {
			cfg.outWarmup.print(warmlog)
		}
		val measlog = new CSVLog(cfg.CSVcolumns,cfg.delimiter)
		try {
			measure(measlog)
		} finally {
			cfg.out.print(measlog)
		}
	}
	
	def abstract void warmup(CSVLog log)
	def abstract void measure(CSVLog log)
	
}