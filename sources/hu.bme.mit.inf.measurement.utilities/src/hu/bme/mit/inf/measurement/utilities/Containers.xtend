package hu.bme.mit.inf.measurement.utilities

import org.eclipse.xtend.lib.annotations.Data

@Data
class Quad<T1,T2,T3,T4> {
	val T1 fisrt
	val T2 second
	val T3 third
	val T4 forth
}
@Data
class Triple<T1,T2,T3> {
	val T1 fisrt
	val T2 second
	val T3 third
}