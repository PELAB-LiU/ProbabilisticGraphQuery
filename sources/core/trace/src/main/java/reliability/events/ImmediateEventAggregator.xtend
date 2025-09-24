package reliability.events

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator
import com.google.common.collect.HashMultiset
import hu.bme.mit.delta.mdd.MddHandle
import reliability.mdd.MddHandleMultiset
import java.util.stream.Stream
import org.eclipse.xtend.lib.annotations.Accessors
import reliability.intreface.ExecutionTime
import reliability.intreface.Configuration
import flight.CollectorGetAggregateEvent
import flight.CollectorUpdateEvent
import flight.OrGetAggregateEvent
import reliability.mdd.MddModel
import flight.OrUpdateEvent
import flight.NotGetAggregateEvent
import flight.NotUpdateEvent

class ImmediateMddCollectorOperator extends MddCollectorOperator {
	override getAggregate(HashMultiset<MddHandle> result) {
		var boolean ignored
		val event = new CollectorGetAggregateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (Configuration.isCancelled) {
				return null;
			}

			if (result === null || result.isNeutral) {
				return null
			}
			val res = new MddHandleMultiset(result)

			return res
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}

	}

	override update(HashMultiset<MddHandle> oldResult, Event updateValue, boolean isInsertion) {
		var boolean ignored
		val event = new CollectorUpdateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (isInsertion) {
				oldResult.add(updateValue.handle)
			} else {
				oldResult.remove(updateValue.handle)
			}
			return oldResult
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}

	}
}

class ImmediateMddOrOperator extends MddOrOperator<CachedBuffer<HashMultiset<Event>, Event>> {
	override getAggregate(CachedBuffer<HashMultiset<Event>, Event> buffer) {
		var boolean ignored
		val event = new OrGetAggregateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (Configuration.isCancelled) {
				return null;
			}

			val result = buffer.cacheOrCompute [ data |
				val handle = data.elementSet.fold(MddModel.INSTANCE.getHandleOf(false), [ accu, entry |
					accu.union(entry.handle)
				])
				new SimpleEvent(handle)
			]

			return result
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}

	override isNeutral(CachedBuffer<HashMultiset<Event>, Event> buffer) {
		return buffer.buffer.isEmpty
	}

	override update(CachedBuffer<HashMultiset<Event>, Event> buffer, Event updateValue, boolean isInsertion) {
		var boolean ignored
		val event = new OrUpdateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (isInsertion) {
				buffer.buffer.add(updateValue)
			} else {
				buffer.buffer.remove(updateValue)
			}
			buffer.invalidate
			return buffer
		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}

	override createNeutral() {
		return new CachedBuffer<HashMultiset<Event>, Event>(HashMultiset.<Event>create)
	}
}

class ImmediateMddNotOperator extends MddNotOperator<CachedBuffer<HashMultiset<Event>, Event>> {

	override createNeutral() {
		return new CachedBuffer<HashMultiset<Event>, Event>(HashMultiset.<Event>create)
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
	override getAggregate(CachedBuffer<HashMultiset<Event>, Event> buffer) {
		var boolean ignored
		val event = new NotGetAggregateEvent
		event.begin
		try {
			ignored = ExecutionTime.start
			if (Configuration.isCancelled) {
				return null;
			}

			val result = buffer.cacheOrCompute [ data |
				val handle = data.elementSet.fold(MddModel.INSTANCE.getHandleOf(false), [ accu, entry |
					accu.union(entry.handle)
				])
				new SimpleEvent(MddModel.INSTANCE.getHandleOf(true).minus(handle))
			]

			return result

		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}

	override isNeutral(CachedBuffer<HashMultiset<Event>, Event> buffer) {
		return buffer.buffer.isEmpty
	}

	override update(CachedBuffer<HashMultiset<Event>, Event> buffer, Event updateValue, boolean isInsertion) {
		var boolean ignored
		val event = new NotUpdateEvent
		event.begin
		try {
			ignored = ExecutionTime.start

			if (isInsertion) {
				buffer.buffer.add(updateValue)
			} else {
				buffer.buffer.remove(updateValue)
			}
			buffer.invalidate
			return buffer

		} finally {
			ExecutionTime.stop(ignored)
			event.commit
		}
	}
}
