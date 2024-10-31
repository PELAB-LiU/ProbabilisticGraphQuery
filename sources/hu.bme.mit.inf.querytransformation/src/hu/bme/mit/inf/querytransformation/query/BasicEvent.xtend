package hu.bme.mit.inf.querytransformation.query

import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationValidator
import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationParameter


class BasicEvent extends PatternAnnotationValidator{
	static val NAME = "BasicEvent"
	static val DESCRIPTION = "Define a basic event for each match of this query."
	static val PROBABILITY = new PatternAnnotationParameter("probability", PatternAnnotationParameter.DOUBLE,"Probability of the basic event.",false,true)
	new(){
		super(NAME, DESCRIPTION, PROBABILITY)
	}
	
	
}