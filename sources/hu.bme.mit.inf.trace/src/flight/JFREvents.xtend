package flight

import jdk.jfr.Event
import org.eclipse.xtend.lib.annotations.Accessors
//TODO reliability.cache
/**
 * Events for reliability.cache.ReliabilityCacheManager
 */
class AccessSessionCacheEvent extends Event{
	@Accessors var boolean cached = true
}
class InvalidateSessionCacheEvent extends Event{
	@Accessors var boolean all = false
}
/**
 * Events for stochastic event wrappers
 */
class WrapperCalculationEvent extends Event{
	@Accessors var String type
}
class WrapperEqualsEvent extends Event{
	@Accessors var String type
	@Accessors var String other
}

/**
 * Events for reliability.interface.D.java
 */
 class AndInterfaceEvent extends Event{
 	@Accessors var int argc = 0
 }
 class WeightInterfaceEvent extends Event{
 	@Accessors var int size = 0
 }
/**
 * Events for reliability.mdd.LogicOperations.xtend
 */
class OrUpdateEvent extends Event {}
class OrGetAggregateEvent extends Event{}

class CollectorUpdateEvent extends Event {}
class CollectorGetAggregateEvent extends Event{}

class NotUpdateEvent extends Event {}
class NotGetAggregateEvent extends Event{}
/**
 * Events for reliability.mdd.MddModel.xtend
 */
class MddVarableCreationEvent extends Event{
	@Accessors var boolean recycled = false
}
class MddUpdateUpdateEvent extends Event{}
class MddTraverseCalculationEvent extends Event{
	@Accessors var boolean cached = false
}
class MddSynchronizationEvent extends Event{}
class MddSynchronizationRemoveEvent extends Event{
	@Accessors var int traces = 0
}
class MddSynchronizationUpdateEvent extends Event{
	@Accessors var int traces = 0
}
class MddSynchronizationInsertEvent extends Event{
	@Accessors var int traces = 0
	var int reflections = 0
	def reflection(){reflections++}
}