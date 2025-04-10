package reliability.mdd

import hu.bme.mit.delta.mdd.MddHandle
import com.google.common.collect.HashMultiset
import com.google.common.collect.Multiset

/**
 * Interface to hide implementation of data source for KOf operation
 */
interface MddHandleCollection {
	def Multiset<MddHandle> getMultiset()
}

class MddHandleMultiset implements MddHandleCollection{
	val HashMultiset<MddHandle> data = HashMultiset.<MddHandle>create
	new() {}
	new(HashMultiset<MddHandle> data) {
		this.data.addAll(data)
	}
	override getMultiset() {
		return data
	}
	override hashCode(){
		data.hashCode
	}
	override equals(Object other){
		if(other instanceof MddHandleMultiset)
			return data.equals(other.data)
		return false
	}
}