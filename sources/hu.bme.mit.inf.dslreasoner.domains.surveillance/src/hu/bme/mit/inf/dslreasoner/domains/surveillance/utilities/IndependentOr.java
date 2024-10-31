package hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities;


import java.util.ArrayList;
import java.util.stream.Stream;

import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.BoundAggregator;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory;
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IMultisetAggregationOperator;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

@SuppressWarnings("restriction")
@AggregatorType(
	parameterTypes = {Double.class},
	returnTypes = {Double.class}
)
public class IndependentOr implements IAggregatorFactory{
	private static InnerAggregator INSTANCE = new InnerAggregator();
	@Override
	public BoundAggregator getAggregatorLogic(Class<?> domainClass) {
		return new BoundAggregator(INSTANCE, domainClass, domainClass);
	}
	
	public static class InnerAggregator implements IMultisetAggregationOperator<Double,Multiset<Double>,Double>{

		@Override
		public String getShortDescription() {
			return "test";
		}

		@Override
		public String getName() {
			return "MyAggregator";
		}

		@Override
		public Double aggregateStream(Stream<Double> stream) {
			throw new RuntimeException("Unimplemented method");
		}

		@Override
		public Multiset<Double> createNeutral() {
			return HashMultiset.<Double>create();
		}

		@Override
		public boolean isNeutral(Multiset<Double> result) {
			return result.isEmpty();
		}

		@Override
		public Multiset<Double> update(Multiset<Double> oldResult, Double updateValue, boolean isInsertion) {
			if(isInsertion) {
				oldResult.add(updateValue);
			} else {
				oldResult.remove(updateValue);
			}
			return oldResult;
		}
 
		@Override
		public Double getAggregate(Multiset<Double> result) {
			if(result == null)
				return null;
			if(isNeutral(result))
				return null;
			Double intermediate = 0.0;
			for(Double entry : result.elementSet()){
				intermediate = 1-((1-intermediate)*(1-entry));
			}
			return intermediate;
		}

	}
}