package reliability.events

import hu.bme.mit.delta.mdd.MddHandle

interface Event {
	def MddHandle getHandle()
}

interface ModifiableEvent {
	def void add(Event event)
	def void remove(Event event)
}

class SimpleEvent implements Event{
	val MddHandle event
	new(MddHandle event){
		this.event = event
	}
	
	override getHandle() {
		return event
	}
	override equals(Object other){
		if(other instanceof Event){
			return event.equals(other.handle)
		} else {
			return false
		}	
	}
	override hashCode(){
		handle.hashCode
	}
}