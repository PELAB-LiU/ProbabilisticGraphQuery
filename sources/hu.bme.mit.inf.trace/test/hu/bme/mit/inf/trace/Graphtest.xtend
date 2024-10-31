package hu.bme.mit.inf.trace

import org.junit.Test
import org.junit.Assert
import com.google.common.collect.HashMultiset
import java.util.Arrays
import hu.bme.mit.delta.java.mdd.JavaMddFactory
import hu.bme.mit.delta.mdd.LatticeDefinition
import hu.bme.mit.delta.java.mdd.MddVariableDescriptor
import java.util.List
import hu.bme.mit.delta.mdd.MddBuilder
import hu.bme.mit.delta.mdd.MddInterpreter

class Graphtest {
	@Test
	def void graphtest() {
		val range = 0..2
		for(i : range)
			println(i)
		Assert.assertTrue(true)
	}
	@Test
	def void multisetTest(){
		val ms = HashMultiset.<String>create()
		ms.add("a", 2);
		ms.add("b")
		ms.add("b")
		println(ms.size)
		println(Arrays.toString(ms.toArray))
	}
	@Test
	def void minusTest(){
		val graph = JavaMddFactory.getDefault.createMddGraph(LatticeDefinition.forSets())
		val order = JavaMddFactory.getDefault().createMddVariableOrder(graph)
		
		val variable0 = order.createOnTop(MddVariableDescriptor.create(order.size, 2))
		val signature0 = order.createSignatureFromVariables(List.of(variable0))
		val handle0 = (new MddBuilder(signature0)).build(#[1], true)
		
		val variable1 = order.createOnTop(MddVariableDescriptor.create(order.size, 2))
		val signature1 = order.createSignatureFromVariables(List.of(variable1))
		val handle1 = (new MddBuilder(signature1)).build(#[1], true)
		
		val False = graph.getHandleFor(false)
		val True = graph.getHandleFor(true)
		
		val handle = True.minus(handle0.intersection(handle1))
		
		MddInterpreter.forEachNonzeroTuple(handle, [tuple,terminal | 
			println(Arrays.toString(tuple))
			true
		],Boolean)
		
	}
}