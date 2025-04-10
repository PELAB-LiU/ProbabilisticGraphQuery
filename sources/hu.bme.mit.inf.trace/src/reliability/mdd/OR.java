package reliability.mdd;

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.BoundAggregator;

import reliability.events.Event;
import reliability.events.MddOrOperator;
import reliability.events.ImmediateMddOrOperator;

@SuppressWarnings("restriction")
@AggregatorType(
	parameterTypes = {Event.class},
	returnTypes = {Event.class}
)
public class OR implements IAggregatorFactory{
	@SuppressWarnings("rawtypes")
	private static MddOrOperator INSTANCE = new ImmediateMddOrOperator();
	//private static MddOrOperator INSTANCE = new DelayedMddOrOperator();
	@Override
	public BoundAggregator getAggregatorLogic(Class<?> domainClass) {
		return new BoundAggregator(INSTANCE, domainClass, domainClass);
	}
	
}

