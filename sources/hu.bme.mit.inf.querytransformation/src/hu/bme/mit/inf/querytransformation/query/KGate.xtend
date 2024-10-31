package hu.bme.mit.inf.querytransformation.query

import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationValidator
import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationParameter
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.IAggregatorFactory
import org.eclipse.viatra.query.runtime.matchers.psystem.aggregations.AggregatorType

class Weight extends PatternAnnotationValidator{
	static val NAME = "KGate"
	static val DESCRIPTION = "TODO"
	static val PROBABILITY = new PatternAnnotationParameter("weights", PatternAnnotationParameter.VARIABLEREFERENCE,"Probability of the basic event.",false,true)
	new(){
		super(NAME, DESCRIPTION, PROBABILITY)
	}
	
	
}

@AggregatorType(parameterTypes = Void, returnTypes = Double)
final class KGate implements IAggregatorFactory {
	
	override getAggregatorLogic(Class<?> domainClass) {
		throw new UnsupportedOperationException("KGate is only a placeholder prior to transformation.")
	}
	
}