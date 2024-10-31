package hu.bme.mit.inf.querytransformation.query

import org.eclipse.viatra.query.patternlanguage.emf.vql.Annotation
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel

class TransformationUtilities {
	protected val g = new VQLParser // generator
	
	def int reducedParameterCount(Pattern pattern){
		if(pattern.probabilitySourceType===PSource.PARAMETER){
			pattern.parameters.size -1
		} else {
			pattern.parameters.size
		}
	}
	def int requiredPatternMaxArgCount(PatternModel model){
		return model.patterns
			.filter[pattern | pattern.isBasicEventDefinition]
			.fold(0, [max, pattern | 
				Math.max(max, pattern.reducedParameterCount)
			])	
	}
	def Iterable<Pattern> filterRequiredArg(PatternModel model, int count){
		return model.patterns
			.filter[pattern | pattern.isBasicEventDefinition]
			.filter[pattern | pattern.reducedParameterCount===count]
	}
	def int countRequiredArg(PatternModel model, int count){
		return model.patterns
			.filter[pattern | pattern.isBasicEventDefinition]
			.fold(0, [cnt, pattern | 
				if(pattern.probabilitySourceType===PSource.PARAMETER){
					(pattern.parameters.size -1)===count ? cnt+1 : cnt
				} else {
					pattern.parameters.size===count ? cnt+1 : cnt
				}
			])	
	}
	
	
	
	
	def boolean isEventBlocked(Pattern pattern) {
		pattern.annotations.exists[annotation|"NoEvent".equals(annotation.name)]
	}
	
	def boolean isBasicEventDefinition(Pattern pattern) {
		pattern.annotations.exists[annotation|"BasicEvent".equals(annotation.name)]
	}

	
	
	def boolean isKGateConstraint(Constraint constraint) {
		if(constraint instanceof CompareConstraint){
			val aggregator = constraint.rightOperand
			if (aggregator instanceof AggregatedValue) {
				return "KGate".equals(aggregator.aggregator.simpleName)
			}
		}
		return false
	}
	
	def boolean isWeightAnnotation(Annotation annotation, String lhsname) {
		if(!"Weight".equals(annotation.name))
			return false
		val lhsparameter = annotation.parameters.findFirst [ param |
			"lhsname".equals(param.name)
		].value as StringValue
		return  lhsname.equals(lhsparameter.value)
	}
	def PSource getProbabilitySourceType(Pattern pattern){
		val value = pattern.annotations.findFirst[annotation|"BasicEvent".equals(annotation.name)].parameters.findFirst [ param |
			"probability".equals(param.name)
		].value
		if(value instanceof VariableReference) return PSource.PARAMETER
		if(value instanceof NumberValue) return PSource.ANNOTATION
		return null
	}
	def VariableReference getProbabilityVariable(Pattern pattern){
		val value = pattern.annotations.findFirst[annotation|"BasicEvent".equals(annotation.name)].parameters.findFirst [ param |
			"probability".equals(param.name)
		].value as VariableReference
		return value
	}
	def double getProbability(Pattern pattern) {
		val value = pattern.annotations.findFirst[annotation|"BasicEvent".equals(annotation.name)].parameters.findFirst [ param |
			"probability".equals(param.name)
		].value as NumberValue
		Double.parseDouble(value.value.value)
	}
	
	
	
	protected enum PSource{
		ANNOTATION,
		PARAMETER
	}
}