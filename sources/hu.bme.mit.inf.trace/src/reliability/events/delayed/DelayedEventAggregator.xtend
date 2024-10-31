package reliability.events.delayed

import hu.bme.mit.delta.mdd.MddHandle
import java.util.stream.Stream
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator
import java.util.stream.Collectors
import java.util.Map
import java.util.HashMap
import org.eclipse.xtend.lib.annotations.Accessors
import com.google.common.collect.HashMultiset
import flight.CollectorGetAggregateEvent
import flight.CollectorUpdateEvent
import flight.NotUpdateEvent
import flight.NotGetAggregateEvent
import flight.OrGetAggregateEvent
import flight.OrUpdateEvent
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import reliability.events.MddOrOperator
import reliability.events.Event
import reliability.events.MddNotOperator

/**
 * Set may need to be changed due to  multiplicity reasons
 */
class DelayedMddOrOperator extends MddOrOperator<DelayedOrEvent> {
	static val neutral = new DelayedOrEvent(HashMultiset.<Event>create())

	override createNeutral() {
		return new DelayedOrEvent(neutral)
	}

	override getAggregate(DelayedOrEvent result) {
		var boolean ignored
		val event = new OrGetAggregateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (Configuration.isCancelled) {
				return null;
			}
			val res = new DelayedOrEvent(result)
			// if(result === null || result.neutral)
			// return null
			// val start = System.nanoTime
			// if(result.aggregate===null){
			// var temp = MddModel.INSTANCE.getHandleOf(false)
			// for(entry : result.values.elementSet){
			// temp = temp.union(entry)
			// }
			// result.aggregate = temp
			// }
			// val end = System.nanoTime
			// println(result.values.size)
			// agtime+=(end-start)/1000.0/1000
			// agcalls++
			// print
			// return result.aggregate
			return res
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}

	override isNeutral(DelayedOrEvent result) {
		return result.equals(neutral)
	}

	override update(DelayedOrEvent oldResult, Event updateValue, boolean isInsertion) {
		var boolean ignored
		val event = new OrUpdateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (isInsertion) {
				oldResult.add(updateValue)
			} else {
				oldResult.remove(updateValue)
			}
			// if(isInsertion){
			// if(!oldResult.values.contains(updateValue)){
			// oldResult.aggregate = oldResult.aggregate===null ? updateValue : oldResult.aggregate.union(updateValue)
			// }
			// oldResult.values.add(updateValue)
			// } else {
			// oldResult.values.remove(updateValue)
			// if(!oldResult.values.contains(updateValue)){
			// oldResult.aggregate = null
			// }
			// }
			return oldResult
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}
}

class DelayedMddNotOperator extends MddNotOperator<DelayedNotEvent> {
	static val neutral = new DelayedNotEvent(HashMultiset.<Event>create())

	override createNeutral() {
		return new DelayedNotEvent(neutral)
	}

	/*
	 * Example:
	 * e(a,0):A,e(a,1):B,e(a,2):C
	 * -------------NEG-------------
	 * !e(a,0):!A,!e(a,1):!B,!e(a,2):!C
	 * -------------AGG-------------
	 * e(a,_):A|B|C
	 * !e(a,_):!(A|B|C)
	 */
	override getAggregate(DelayedNotEvent result) {
		var boolean ignored
		val event = new NotGetAggregateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (Configuration.isCancelled) {
				return null;
			}
			val res = new DelayedNotEvent(result)

//		if(result === null)
//			return null
//		if(result.values.isEmpty)
//			return MddModel.INSTANCE.getHandleOf(true);
//			
//		val start = System.nanoTime
//		
//		if(result.aggregate===null){
//			val intermediate = result.intermediate!==null ? result.intermediate :
//				{
//					var temp = MddModel.INSTANCE.getHandleOf(false)
//					for(entry : result.values.elementSet){
//						temp = temp.union(entry)
//					}
//					result.intermediate = temp
//					temp
//				}
//			result.aggregate =  MddModel.INSTANCE.getHandleOf(true).minus(intermediate)
//		}
//		val end = System.nanoTime
//		println(result.values.size)
//		agtime+=(end-start)/1000.0/1000
//		agcalls++
//		print
//		return result.aggregate
			return res
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}

	override isNeutral(DelayedNotEvent result) {
		return result.equals(neutral)
	}

	override update(DelayedNotEvent oldResult, Event updateValue, boolean isInsertion) {
		var boolean ignored
		val event = new NotUpdateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (isInsertion) {
				oldResult.add(updateValue)
			} else {
				oldResult.remove(updateValue)
			}
//		if(isInsertion){
//			if(!oldResult.values.contains(updateValue)){
//				oldResult.intermediate = oldResult.intermediate===null ? updateValue : oldResult.intermediate.union(updateValue)
//				oldResult.aggregate = null
//			}
//			oldResult.values.add(updateValue)
//		} else {
//			oldResult.values.remove(updateValue)
//			if(!oldResult.values.contains(updateValue)){
//				oldResult.intermediate = null
//				oldResult.aggregate = null
//			}
//		}
			return oldResult
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}
}
