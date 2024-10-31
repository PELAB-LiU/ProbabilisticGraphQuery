package reliability.events

import com.google.common.collect.HashMultiset
import hu.bme.mit.delta.mdd.MddHandle
import reliability.mdd.MddHandleMultiset
import org.eclipse.xtend.lib.annotations.Accessors
import java.util.stream.Stream
import reliability.intreface.ExecutionTime
import java.util.stream.Collectors
import reliability.mdd.MddModel
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator
import org.eclipse.xtext.xbase.lib.Functions.Function1

abstract class MddCollectorOperator implements IMultisetAggregationOperator<Event, HashMultiset<MddHandle>, MddHandleMultiset> {
	@Accessors(PUBLIC_GETTER) static var totalcalctime = 0.0
	
	override aggregateStream(Stream<Event> stream) {
		var boolean ignored
		try {
			ignored = ExecutionTime.start
		val wrapper = new MddHandleMultiset();
		stream.iterator.fold(
			wrapper,
			[set, element | 
				set.multiset.add(element.handle)
				set
			]
		)
		return wrapper
		}finally {
			ExecutionTime.stop(ignored)
		}
		
	}
	
	override createNeutral() {
		return HashMultiset.<MddHandle>create
	}
	
	override getName() {
		return "collect"
	}
	
	override getShortDescription() {
		return "Aggregates the arguments to a set."
	}
	
	override isNeutral(HashMultiset<MddHandle> result) {
		return result.isEmpty
	}
}

abstract class MddOrOperator<Buffer> implements IMultisetAggregationOperator<Event, Buffer, Event>{
	override aggregateStream(Stream<Event> stream) {
		var boolean ignored
		try {
			ignored = ExecutionTime.start
		val setdata = stream.collect(Collectors.toSet)
		
		val result = setdata.fold(MddModel.INSTANCE.getHandleOf(false),
			[accu, entry | accu.union(entry.handle)]
		)
		
		return (new SimpleEvent(result) as Event)
		}finally {
			ExecutionTime.stop(ignored)
		}
	}
	
	override getName() {
		return "or"
	}
	
	override getShortDescription() {
		return "Joins MddHandles with or relation."
	}
}

abstract class MddNotOperator<Buffer> implements IMultisetAggregationOperator<Event, Buffer, Event>{
	override aggregateStream(Stream<Event> stream) {
		var boolean ignored
		try {
			ignored = ExecutionTime.start
		val setdata = stream.collect(Collectors.toSet)
		
		var temp = setdata.fold(
			MddModel.INSTANCE.getHandleOf(false),
			[acc, current | acc.union(current.handle)]
		)
		val result = MddModel.INSTANCE.getHandleOf(true).minus(temp)
		
		return new SimpleEvent(result)
		}finally {
			ExecutionTime.stop(ignored)
		}
	}
	
	override getName() {
		return "not"
	}
	
	override getShortDescription() {
		return "Negates a stochastic pattern."
	}
}

class CachedBuffer<B,C>{
	val B buffer;
	var C cache;
	var boolean valid
	new(B buffer){
		this.buffer = buffer
		valid = false
	}
	def getBuffer() {
		return buffer
	}
	def C getLastCache(){
		return cache
	}
	def C cacheOrCompute(Function1<B,C> update){
		if(! valid){
			cache = update.apply(buffer)
			valid = true;
		}
		return cache
	}
	def invalidate(){
		valid = false
	}
}









