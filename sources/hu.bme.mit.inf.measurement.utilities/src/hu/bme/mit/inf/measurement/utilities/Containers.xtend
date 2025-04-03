package hu.bme.mit.inf.measurement.utilities

import org.eclipse.xtend.lib.annotations.Data

@Data
class UniformQuad<T1> extends Quad<T1,T1,T1,T1> {
	new(T1 fisrt, T1 second, T1 third, T1 forth) {
		super(fisrt, second, third, forth)
	}
}
@Data
class Quad<T1,T2,T3,T4> {
	val T1 fisrt
	val T2 second
	val T3 third
	val T4 forth
}
@Data
class UniformTriple<T1> extends Triple<T1,T1,T1> {
	new(T1 fisrt, T1 second, T1 third) {
		super(fisrt, second, third)
	}
}
@Data
class Triple<T1,T2,T3> {
	val T1 fisrt
	val T2 second
	val T3 third
}