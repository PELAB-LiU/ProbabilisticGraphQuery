package reliability.cache

import org.eclipse.xtend.lib.annotations.Accessors
import hu.bme.mit.delta.mdd.MddHandle
import java.util.List

interface ReliabilityCacheEntry {
	def double getProbability()
	def boolean isValid()
}

class ReliabilityCacheNode implements ReliabilityCacheEntry{
	@Accessors(PACKAGE_GETTER) val List<ReliabilityCacheNode> affects = newArrayList
	@Accessors(PUBLIC_GETTER) var double probability = 0.0
	@Accessors(PUBLIC_GETTER) var boolean valid  = false
	
	def void update(double value){
		invalidate()
		valid = true
		probability = value
	}
	def void invalidate(){
		if(valid){
			valid = false
			affects.forEach[node | node.invalidate]
		}
	}
}

