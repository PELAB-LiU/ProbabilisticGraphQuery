package reliability.mdd

import hu.bme.mit.delta.mdd.MddGraph
import hu.bme.mit.delta.java.mdd.JavaMddFactory
import hu.bme.mit.delta.mdd.LatticeDefinition
import hu.bme.mit.delta.mdd.MddVariableOrder
import hu.bme.mit.delta.mdd.MddVariable
import hu.bme.mit.delta.mdd.MddHandle
import java.util.Set
import hu.bme.mit.delta.mdd.MddVariableDescriptor
import java.util.List
import hu.bme.mit.delta.mdd.MddBuilder
import java.util.HashMap
import java.util.HashSet
import tracemodel.TraceModel
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine
import tracemodel.Trace
import org.eclipse.emf.ecore.EObject
import tracemodel.TracemodelFactory
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import tracemodel.TracemodelPackage
import org.eclipse.emf.ecore.EPackage
import reliability.cache.ReliabilityCacheManager
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.IPatternMatch
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import tracemodel.Trace1
import tracemodel.Trace2
import reliability.events.SimpleEvent
import flight.MddUpdateUpdateEvent
import flight.MddTraverseCalculationEvent
import flight.MddSynchronizationEvent
import flight.MddSynchronizationRemoveEvent
import flight.MddSynchronizationUpdateEvent
import flight.MddSynchronizationInsertEvent
import flight.MddVarableCreationEvent
import reliability.cache.SessionCache
import reliability.intreface.CacheMode
import reliability.cache.NoCacheManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MddModel {
	static val Logger LOG4J = LoggerFactory.getLogger(MddModel);
	static val INSTANCES = #{ "incremental" -> new MddModel, "standalone" -> new MddModel}
	public static var MddModel INSTANCE = null
	def static void changeTo(String target){
		INSTANCE = INSTANCES.get(target)
	}
	def static MddModel getInstanceOf(String target){
		return INSTANCES.get(target)
	}
	
	val TraceModel model
	
	
	
	val Set<MddTerminalEntry> entries
	val Set<MddTerminalEntry> inactiveEntries
	
	//moved to TraceModel.probabilities(_,internal_probabilities);
	//val Map<MddVariable, Double> probabilities
	
	@Accessors(PUBLIC_GETTER, PUBLIC_SETTER) var ReliabilityCacheManager cacheManager
	//val Map<String, IQuerySpecification<? extends ViatraQueryMatcher>> matchers
	val Set<IQuerySpecification<? extends ViatraQueryMatcher>> insertionSpecification
	val Set<IQuerySpecification<? extends ViatraQueryMatcher>> updateSpecification
	val Set<IQuerySpecification<? extends ViatraQueryMatcher>> removeSpecification
	def registerSpecificationIfNeeded(IQuerySpecification<? extends ViatraQueryMatcher> spc){
		graph.uniqueTableSize
		if(spc.getFullyQualifiedName.matches("^Insertion[1-9]\\d*$")){
			insertionSpecification.add(spc)
			return
		}
		if(spc.getFullyQualifiedName.matches("^Removal[1-9]\\d*$")){
			removeSpecification.add(spc)
			return
		}
		if(spc.getFullyQualifiedName.matches("^Update[1-9]\\d*$")){
			updateSpecification.add(spc)
			return
		}
		/*switch (spc.getFullyQualifiedName) {
			case "unaryRemoval": {
				matchers.put("unaryRemoval", spc)
				return
			}
			case "unaryUpdate": {
				matchers.put("unaryUpdate", spc)
				return
			}
			case "unaryInsertion": {
				matchers.put("unaryInsertion", spc)
				return
			}
			default: {}
		}*/
	}
	def void resetModel(){
		graph = JavaMddFactory.getDefault.createMddGraph(LatticeDefinition.forSets())
		order = JavaMddFactory.getDefault().createMddVariableOrder(graph)
		entries.clear
		inactiveEntries.clear
		model.probabilities = new ProbabilityMap
		model.traces.clear
		model.setMddFalse(new SimpleEvent(getHandleOf(false)));
		model.setMddTrue(new SimpleEvent(getHandleOf(true)));
	}
	private new(){
		graph = JavaMddFactory.getDefault.createMddGraph(LatticeDefinition.forSets())
		order = JavaMddFactory.getDefault().createMddVariableOrder(graph)
		
		entries = newHashSet
		inactiveEntries = newHashSet
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE);
		
		val tracefactory = TracemodelFactory.eINSTANCE;
		model = tracefactory.createTraceModel();
		model.probabilities = new ProbabilityMap
		model.setMddFalse(new SimpleEvent(getHandleOf(false)));
		model.setMddTrue(new SimpleEvent(getHandleOf(true)));
		
		//cacheManager = new ReliabilityCacheManager
		insertionSpecification = newHashSet
		updateSpecification = newHashSet
		removeSpecification = newHashSet
		//matchers = newHashMap
	} 
	
	private def MddTerminalEntry createVariable(double probability){
		val event = new MddVarableCreationEvent
		event.begin
		if(!inactiveEntries.isEmpty){
			//Reuse entry
			val entry = inactiveEntries.get(0)
			inactiveEntries.remove(entry)
			model.updateProbability(entry.variable, probability)
			event.recycled = true
			event.commit
			return entry
		}
		val variable = order.createOnTop(MddVariableDescriptor.create(order.size, 2))
		val signature = order.createSignatureFromVariables(List.of(variable))
		val handle = (new MddBuilder(signature)).build(#[1], true)
		
		val terminal = new MddTerminalEntry(variable, handle)
		entries.add(terminal)
		model.updateProbability(variable, probability)
		event.commit
		return terminal
	}
	def updateProbability(TraceModel model, MddVariable variable, double probability){
		val event = new MddUpdateUpdateEvent
		event.begin
		val newmap = new ProbabilityMap(model.probabilities);
		newmap.put(variable, probability)
		model.probabilities = newmap; //Trigger update event for viatra
		event.commit
	}
	def MddHandle getHandleOf(boolean value){
		return graph.getHandleFor(value)
	}
	
	def long getTableSize(){
		graph.uniqueTableSize
	}
	
	def double getValue(MddHandle root, ReliabilityCacheManager cache){
		if(root.isTerminal){
			return (root.data as Boolean) ? 1 : 0
		}
		val event = new MddTraverseCalculationEvent
		event.begin
		
		val cacheEntry = cache.getOrCreateNode(root, 2)
		
		if(cacheEntry.valid){
			event.cached = true
			event.commit
			return cacheEntry.probability
		}
		var result = 0.0
		val node = model.probabilities.get(root.variableHandle.variable.get)
		
		result += (1-node) * getValue(root.get(0).toLowestSignificantVariable, cache);
		result += node * getValue(root.get(1).toLowestSignificantVariable, cache);
		cache.updateNode(root, result)
		
		event.commit
		return result	
	}
	def TraceModel getTraceModel(){
		return model
	}
	def long unaryForAll(ViatraQueryEngine engine) {
		LOG4J.debug("Unary")
		val start = System.nanoTime
		val event = new MddSynchronizationEvent
		event.begin;
		val end = (engine as AdvancedViatraQueryEngine).delayUpdatePropagation [
			removeUnaryForAll(engine);
			updateUnaryForAll(engine);
			insertUnaryForAll(engine);
			System.nanoTime
		]
		event.commit
		
		return end-start
	}
	
	private def removeUnaryForAll(ViatraQueryEngine engine) {
		val event = new MddSynchronizationRemoveEvent
		event.begin
		val tracesToRemove = new HashSet<Trace>()
		for(specification : removeSpecification){
			val matcher = engine.getMatcher(specification)
			matcher.forEachMatch([ IPatternMatch match |
				val trace = match.get(0) as Trace
				tracesToRemove.add(trace)
				cacheManager?.removeNode(trace.event.handle)
			])
			model.getTraces().removeAll(tracesToRemove)
			tracesToRemove.forEach[trace |
				val te = entries.findFirst[MddTerminalEntry entry |
					val handle = entry.handle
					val hc = handle.hashCode() 
					hc  === trace.event.hashCode
				]
				inactiveEntries.add(te)
			]
			}
		LOG4J.info("Removed {}", tracesToRemove.size)
		event.traces = tracesToRemove.size
		event.commit
	}
	private def updateUnaryForAll(ViatraQueryEngine engine) {
		val event = new MddSynchronizationUpdateEvent
		event.begin
		val tracesToUpdate = new HashMap<Trace, Double>();
		for(specification : updateSpecification){
			val matcher = engine.getMatcher(specification)
			matcher.forEachMatch(IPatternMatch match | {
				val trace = match.get(0) as Trace;
				val value = match.get(1) as Double;
				tracesToUpdate.put(trace, value);
			});
			tracesToUpdate.forEach[trace, value|
				trace.setProbability(value)
				val variable = trace.event.handle.variableHandle.variable.get
				model.updateProbability(variable, value)
				cacheManager?.updateVariable(variable)
			]
		}
		LOG4J.info("Updated {}", tracesToUpdate.size)
		event.traces = tracesToUpdate.size
		event.commit
	}
	private def insertUnaryForAll(ViatraQueryEngine engine) {
		val event = new MddSynchronizationInsertEvent
		event.begin
		val factory = TracemodelFactory.eINSTANCE;		
		val newtraces = new HashSet<Trace>();
		for(specification : insertionSpecification){
			val matcher = engine.getMatcher(specification);
			val arity = Integer.parseInt(specification.fullyQualifiedName.replace("Insertion",""))
			matcher.forEachMatch(IPatternMatch match | {
				val name = match.get(arity) as String;
				val from = match.get(arity+1) as Integer;
				val to = match.get(arity+2) as Integer;
				val probability = match.get(arity+3) as Double;
				for(idx : from+1..to){
					var Trace trace = null
					switch(arity){
						case 1: {
							trace = factory.createTrace1()
							(trace as Trace1).arg1 = match.get(0) as EObject
						}
						
						case 2 :{
							trace = factory.createTrace2()
							(trace as Trace2).arg1 = match.get(0) as EObject
							(trace as Trace2).arg2 = match.get(1) as EObject
						}
						
						default: {
							event.reflection
							val factoryMethod = TracemodelFactory.getMethod("createTrace"+arity);
							val t = factoryMethod.invoke(factory) as Trace
							for(i : 1..arity){
								val set = t.getClass().getMethod("setArg"+i,EObject);
								set.invoke(t, match.get(i-1))
							}
						}
					}
					
					trace.setEvent(new SimpleEvent(createVariable(probability).getHandle()));
					trace.generator = name;
					trace.index = idx
					trace.setProbability(probability);
					
					newtraces.add(trace);
				}
			});
		}
		
		model.getTraces().addAll(newtraces);
		LOG4J.info("Insertions {}", newtraces.size)
		event.traces = newtraces.size
		event.commit
	}
	def initializePatterns(ViatraQueryEngine engine){
		insertionSpecification.forEach[engine.getMatcher(it)]
		updateSpecification.forEach[engine.getMatcher(it)]
		removeSpecification.forEach[engine.getMatcher(it)]
	}
	
	val SessionCache globalCache = new SessionCache
	var CacheMode cacheMode = CacheMode.SESSION
	def ReliabilityCacheManager getCacheForSession(){
		return switch cacheMode{
			case SESSION : new SessionCache
			case GLOBAL: globalCache
			case NO : NoCacheManager.INSTANCE
		}
	}
	def invalidateCache(){globalCache.invalidateNode(null)}
	var MddGraph<Boolean> graph
	var MddVariableOrder order 
	
}

@Data
class MddTerminalEntry{
	val MddVariable variable
	val MddHandle handle
}