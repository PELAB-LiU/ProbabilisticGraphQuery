package hu.bme.mit.inf.trace;

import com.google.common.collect.HashMultiset;
import hu.bme.mit.delta.java.mdd.JavaMddFactory;
import hu.bme.mit.delta.java.mdd.MddGraph;
import hu.bme.mit.delta.java.mdd.MddSignature;
import hu.bme.mit.delta.java.mdd.MddVariable;
import hu.bme.mit.delta.java.mdd.MddVariableDescriptor;
import hu.bme.mit.delta.java.mdd.MddVariableOrder;
import hu.bme.mit.delta.mdd.LatticeDefinition;
import hu.bme.mit.delta.mdd.MddBuilder;
import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddInterpreter;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class Graphtest {
  @Test
  public void graphtest() {
    final IntegerRange range = new IntegerRange(0, 2);
    for (final Integer i : range) {
      InputOutput.<Integer>println(i);
    }
    Assert.assertTrue(true);
  }

  @Test
  public void multisetTest() {
    final HashMultiset<String> ms = HashMultiset.<String>create();
    ms.add("a", 2);
    ms.add("b");
    ms.add("b");
    InputOutput.<Integer>println(Integer.valueOf(ms.size()));
    InputOutput.<String>println(Arrays.toString(ms.toArray()));
  }

  @Test
  public void minusTest() {
    final MddGraph<Boolean> graph = JavaMddFactory.getDefault().<Boolean>createMddGraph(LatticeDefinition.forSets());
    final MddVariableOrder order = JavaMddFactory.getDefault().createMddVariableOrder(graph);
    final MddVariable variable0 = order.createOnTop(MddVariableDescriptor.create(Integer.valueOf(order.size()), 2));
    final MddSignature signature0 = order.createSignatureFromVariables(List.<MddVariable>of(variable0));
    final MddHandle handle0 = new MddBuilder<Boolean>(signature0).build(new Integer[] { Integer.valueOf(1) }, Boolean.valueOf(true));
    final MddVariable variable1 = order.createOnTop(MddVariableDescriptor.create(Integer.valueOf(order.size()), 2));
    final MddSignature signature1 = order.createSignatureFromVariables(List.<MddVariable>of(variable1));
    final MddHandle handle1 = new MddBuilder<Boolean>(signature1).build(new Integer[] { Integer.valueOf(1) }, Boolean.valueOf(true));
    final hu.bme.mit.delta.java.mdd.MddHandle False = graph.getHandleFor(Boolean.valueOf(false));
    final hu.bme.mit.delta.java.mdd.MddHandle True = graph.getHandleFor(Boolean.valueOf(true));
    final MddHandle handle = True.minus(handle0.intersection(handle1));
    final MddInterpreter.MddTupleCallback<Boolean> _function = (Integer[] tuple, Boolean terminal) -> {
      boolean _xblockexpression = false;
      {
        InputOutput.<String>println(Arrays.toString(tuple));
        _xblockexpression = true;
      }
      return _xblockexpression;
    };
    MddInterpreter.<Boolean>forEachNonzeroTuple(handle, _function, Boolean.class);
  }
}
