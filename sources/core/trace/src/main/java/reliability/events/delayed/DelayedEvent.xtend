package reliability.events.delayed

import hu.bme.mit.delta.mdd.MddHandle
import java.util.Set
import com.google.common.collect.HashMultiset
import reliability.mdd.MddModel
import java.util.Arrays
import flight.WrapperCalculationEvent
import flight.WrapperEqualsEvent
import reliability.intreface.Configuration
import reliability.intreface.ExecutionTime
import reliability.events.Event
import reliability.events.ModifiableEvent

class OrderedDelayedAndEvent extends AbstractDelayedEvent {
	val Event[] events

	new(Event... events) {
		this.events = events
	}

	override calculateEvent() {
		val handle = events.fold(
			MddModel.INSTANCE.getHandleOf(true),
			[acc, current|acc.intersection(current.handle)]
		)
		return handle
	}

	override equals(Object other) {
		val event = new WrapperEqualsEvent
		event.type = this.class.simpleName
		event.other = other.class.simpleName
		event.begin
		var res = false
		if (other instanceof OrderedDelayedAndEvent) {
			if (isValid && other.isValid) {
				res = handle.equals(other.handle)
			} else {
				res = events.length === other.events.length && Arrays.deepEquals(events, other.events)
			}
		} else {
			res = super.equals(other)
		}
		event.commit
		return res
	}

	override hashCode() {
		Arrays.hashCode(events)
	}
}

class DelayedNotEvent extends DelayedOrEvent {
	new(HashMultiset<Event> template) {
		super(template)
	}

	new(DelayedNotEvent template) {
		super(template)
	}

	override calculateEvent() {
		val tmp = super.calculateEvent()
		val handle = MddModel.INSTANCE.getHandleOf(true).minus(tmp)
		return handle
	}

	override equals(Object other) {
		(other instanceof DelayedNotEvent) && super.equals(other)
	}

}

class DelayedOrEvent extends AbstractDelayedEvent implements ModifiableEvent {
	val HashMultiset<Event> events;

	new(HashMultiset<Event> template) {
		events = HashMultiset.<Event>create(template)
	}

	new(DelayedOrEvent template) {
		super(template)
		events = HashMultiset.<Event>create(template.events)
	}

	override calculateEvent() {
		var temp = events.elementSet.fold(
			MddModel.INSTANCE.getHandleOf(false),
			[acc, current|acc.union(current.handle)]
		)
		return temp
	}

	override add(Event event) {
		if (!events.contains(event))
			invalidateHandle
		events.add(event)
	}

	override remove(Event event) {
		events.remove(event)
		if (!events.contains(event))
			invalidateHandle
	}

	override equals(Object other) {
		val event = new WrapperEqualsEvent
		event.type = this.class.simpleName
		event.other = other.class.simpleName
		event.begin
		var res = false
		if (other instanceof DelayedOrEvent) {
			if (isValid && other.isValid) {
				res = handle.equals(other.handle)
			} else {
				res = events.elementSet.equals(other.events.elementSet)
			}
		} else {
			res = super.equals(other)
		}
		event.commit
		return res
	}

	override hashCode() {
		events.hashCode
	}

}

abstract class AbstractDelayedEvent implements Event {
	var MddHandle event

	new() {
	}

	new(AbstractDelayedEvent template) {
		this.event = template.event
	}

	override getHandle() {
		var boolean ignored
		try {
			ignored = ExecutionTime.start
			Configuration.abortIfCancelled
			if (event === null) {
				val event = new WrapperCalculationEvent
				event.begin
				event = calculateEvent();
				event.type = this.class.simpleName
				event.commit
			}
			return event
		} finally {
			ExecutionTime.stop(ignored)
		}
	}

	abstract def MddHandle calculateEvent()

	def boolean isValid() {
		return event !== null
	}

	def void invalidateHandle() {
		event = null;
	}

	override hashCode() {
		return handle.hashCode();
	}

	override equals(Object other) {
		if (other instanceof Event) {
			val res = handle.equals(other.handle)
			return res
		}
		return false
	}

}
