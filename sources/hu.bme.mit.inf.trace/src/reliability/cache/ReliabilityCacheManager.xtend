package reliability.cache

import hu.bme.mit.delta.mdd.MddVariable
import hu.bme.mit.delta.mdd.MddHandle
import java.util.Map
import java.util.List
import flight.InvalidateSessionCacheEvent
import org.eclipse.viatra.query.patternlanguage.emf.validation.VariableReferenceCount.ReferenceType
import flight.AccessSessionCacheEvent

interface ReliabilityCacheManager {
	def ReliabilityCacheEntry createNode(MddHandle node, int childrens)
	def ReliabilityCacheEntry getOrCreateNode(MddHandle node, int childrens)
	def void updateNode(MddHandle node, double probability)
	def void updateVariable(MddVariable variable)
	def void invalidateNode(MddHandle node)
	def void removeNode(MddHandle node)
}


class SessionCache implements ReliabilityCacheManager {
	val Map<MddHandle, ReliabilityCacheNode> cachedHandles = newHashMap
	override createNode(MddHandle node, int childrens){
		if(node.isTerminal) throw new IllegalArgumentException("Terminal node cannot be cached.")
		if(cachedHandles.containsKey(node)) throw new IllegalArgumentException("Handle is already cached.")
		
		val newNode = new ReliabilityCacheNode()
		cachedHandles.put(node, newNode)
		
		return newNode
	}
	/**
	 * 
	 */
	override getOrCreateNode(MddHandle node, int childrens){
		val event = new AccessSessionCacheEvent
		event.begin
		
		val cached = cachedHandles.get(node)
		if(cached===null){
			event.cached = false
			val res = createNode(node, childrens);
			event.commit
			return res
		}
		event.commit
		return cached
	}
	/**
	 * 
	 */
	override updateNode(MddHandle node, double probability){
		cachedHandles.get(node)?.update(probability)
	}
	
	override updateVariable(MddVariable variable){}
	override invalidateNode(MddHandle node){
		val event = new InvalidateSessionCacheEvent
		event.all = node===null
		event.begin
		if(node===null){
			cachedHandles.clear
		} else {
			cachedHandles.remove(node)
		}
		event.commit
	}
	override removeNode(MddHandle node){}
}

//class GlobalReliabilityCache implements ReliabilityCacheManager {
//	val Map<MddHandle, ReliabilityCacheNode> cachedHandles
//	val Map<MddVariable, List<ReliabilityCacheNode>> variableDependency
//	
//	new(){
//		cachedHandles = newHashMap
//		variableDependency = newHashMap
//	}
//	/**
//	 * 
//	 */
//	
//	override createNode(MddHandle node, int childrens){
//		/**
//		 * Parameter check
//		 */
//		if(node.isTerminal) throw new IllegalArgumentException("Terminal node cannot be cached.")
//		if(cachedHandles.containsKey(node)) throw new IllegalArgumentException("Handle is already cached.")
//		
//		val newNode = new ReliabilityCacheNode()
//		
//		for(i : 0..childrens-1){
//			val lower = node.get(0)
//			if(!lower.isTerminal){
//				val cachenode = getOrCreateNode(lower, childrens) as ReliabilityCacheNode
//				cachenode.affects.add(newNode)
//			}
//		}
//		
//		cachedHandles.put(node, newNode)
//		val variable = node.variableHandle.variable.get
//		if(variable===null) throw new IllegalStateException("Handle has no assigned variable.")
//		variableDependency.putIfAbsent(variable, newArrayList)
//		variableDependency.get(variable).add(newNode)
//		
//		return newNode
//	}
//	/**
//	 * 
//	 */
//	override getOrCreateNode(MddHandle node, int childrens){
//		val cached = cachedHandles.get(node)
//		if(cached===null){
//			return createNode(node, childrens);
//		}
//		return cached
//	}
//	/**
//	 * 
//	 */
//	override updateNode(MddHandle node, double probability){
//		cachedHandles.get(node)?.update(probability)
//	}
//	
//	override updateVariable(MddVariable variable){
//		val dependencies = variableDependency.get(variable)
//		if(dependencies!==null)
//			dependencies.forEach[node | node.invalidate]
//	}
//	/**
//	 * 
//	 */
//	override invalidateNode(MddHandle node){
//		cachedHandles.get(node)?.invalidate
//	}
//	/**
//	 * 
//	 */
//	override removeNode(MddHandle node){
//		//throw new UnsupportedOperationException("Removal is not implemented.")
//	}
//}

class NoCacheManager implements ReliabilityCacheManager {
	public static val INSTANCE = new NoCacheManager
	static class NullReliabilityNode implements ReliabilityCacheEntry {
		override getProbability() {
			return Double.NaN
		}
		override isValid() { false }
	}
	val invariant = new NoCacheManager.NullReliabilityNode
	override createNode(MddHandle node, int childrens){
		return invariant  
	}
	override getOrCreateNode(MddHandle node, int childrens){
		return invariant
	}
	override updateNode(MddHandle node, double probability){}
	override invalidateNode(MddHandle node){}
	override removeNode(MddHandle node){}
	override updateVariable(MddVariable variable) {}
}




