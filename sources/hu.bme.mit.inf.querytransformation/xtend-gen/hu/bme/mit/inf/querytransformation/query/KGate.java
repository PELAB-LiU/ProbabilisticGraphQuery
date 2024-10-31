package hu.bme.mit.inf.querytransformation.query;

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.BoundAggregator;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory;

@AggregatorType(parameterTypes = Void.class, returnTypes = Double.class)
@SuppressWarnings("all")
public final class KGate implements IAggregatorFactory {
  @Override
  public BoundAggregator getAggregatorLogic(final Class<?> domainClass) {
    throw new UnsupportedOperationException("KGate is only a placeholder prior to transformation.");
  }
}
