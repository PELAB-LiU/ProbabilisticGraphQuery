package reliability.mdd;

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.BoundAggregator;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory;

import reliability.events.Event;
import reliability.events.MddNotOperator;
import reliability.events.ImmediateMddNotOperator;

@SuppressWarnings("restriction")
@AggregatorType(
	parameterTypes = { Event.class },
	returnTypes = { Event.class }
)
public class NOT implements IAggregatorFactory {
	@SuppressWarnings("rawtypes")
	private static MddNotOperator INSTANCE = new ImmediateMddNotOperator();

	@Override
	public BoundAggregator getAggregatorLogic(Class<?> domainClass) {
		return new BoundAggregator(INSTANCE, domainClass, domainClass);
	}

}