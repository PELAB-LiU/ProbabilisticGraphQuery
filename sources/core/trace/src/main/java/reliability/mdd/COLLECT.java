package reliability.mdd;

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.BoundAggregator;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory;

import reliability.events.Event;
import reliability.events.ImmediateMddCollectorOperator;
import reliability.events.MddCollectorOperator;

@SuppressWarnings("restriction")
@AggregatorType(
		parameterTypes = {Event.class},
		returnTypes = {MddHandleCollection.class}
	)
public class COLLECT implements IAggregatorFactory{
	//private static MddCollectorOperator INSTANCE = new MddCollectorOperator();
	private static MddCollectorOperator INSTANCE = new ImmediateMddCollectorOperator();
	@Override
	public BoundAggregator getAggregatorLogic(Class<?> domainClass) {
		return new BoundAggregator(INSTANCE, domainClass, domainClass);
	}
	
}
