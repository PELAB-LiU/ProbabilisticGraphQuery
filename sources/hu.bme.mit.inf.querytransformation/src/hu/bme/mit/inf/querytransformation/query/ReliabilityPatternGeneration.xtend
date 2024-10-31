package hu.bme.mit.inf.querytransformation.query

import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern
import java.util.Set
import org.eclipse.emf.ecore.EPackage
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import tracemodel.TracemodelPackage
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall
import java.util.HashSet
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody
import org.eclipse.xtend2.lib.StringConcatenation
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.Annotation
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue

/**
 * UNIMPLEMENTED: negation, not patterns 
 * TODO: collection export
 * 		fix stochastic version
 * TODO: stochastic patterns for basic event definitions (usability in find)
 */

@Deprecated
class ReliabilityPatternGeneration {
	val Set<Pattern> stochasticPatterns = newHashSet
	val Set<Pattern> basicEventPatterns = newHashSet
	val Set<Pattern> needNotPattern = newHashSet
	val g = new VQLParser // generator
	
	def static doSetup(){
		EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE)
	} 
	def test() {
		EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE)
		EMFPatternLanguageStandaloneSetup.doSetup()

		val parsed = PatternParserBuilder.instance.parse(
			Files.readString(
				Path.of(
					"/home/mate/graphquery/hu.bme.mit.inf.dslreasoner.domains.satellite1/src/hu/bme/mit/inf/dslreasoner/domains/satellite1/queries/reliability.vql")))
		if (parsed.hasError || parsed.patterns.empty) {
			println("Input contains errors or empty.")
			parsed.errors.forEach[err|println(err)]
		} else {
			val patternmodel = parsed.patterns.get(0).eContainer as PatternModel
			val stochastics = getStochasticPatterns(patternmodel)

			stochasticPatterns.clear
			stochasticPatterns.addAll(stochastics.key)
			basicEventPatterns.clear
			basicEventPatterns.addAll(stochastics.value)
            
            val vqlcontent = '''
            «generateImports(patternmodel)»
            «generateUserQueries(patternmodel)»
            «generateInterfaceQueries»
            '''
            println(vqlcontent) 
            
            val parsed2 = PatternParserBuilder.instance.parse(vqlcontent)
            
            parsed2.errors.forEach[error | System.err.println(error)]
		}
	}
	def String transformPatternFile(File vql){
		val parsed = PatternParserBuilder.instance.parse(
			Files.readString(Path.of(vql.path)))
		if (parsed.hasError || parsed.patterns.empty) {
			println("Input contains errors or empty.")
			parsed.errors.forEach[err|println(err)]
			throw new RuntimeException
		} else {
			val patternmodel = parsed.patterns.get(0).eContainer as PatternModel
			val stochastics = getStochasticPatterns(patternmodel)

			stochasticPatterns.clear
			stochasticPatterns.addAll(stochastics.key)
			basicEventPatterns.clear
			basicEventPatterns.addAll(stochastics.value)
			println()
            
            val vqlcontent = '''
            «generateImports(patternmodel)»
            «generateUserQueries(patternmodel)»
            «generateInterfaceQueries»
            '''
            println(vqlcontent) 
            
            val parsed2 = PatternParserBuilder.instance.parse(vqlcontent)
            
            parsed2.errors.forEach[error | System.err.println(error)]
            return vqlcontent
		}
	}
	def generateImports(PatternModel model)'''
	/**
	 * Imports for stochastic queries
	 */
	import "http://www.eclipse.org/emf/2002/Ecore"
	import "http://www.example.org/tracemodel"
	
	import java reliability.mdd.COLLECT;
	import java reliability.mdd.OR;
	import java hu.bme.mit.delta.mdd.MddHandle; 
	import java reliability.intreface.D;
	//import java reliability.mdd.MddHandleCollection;
	import java reliability.events.Event;
	
	/**
	 * Imports for weight functions
	 */
	«generateWeightImports(model)»
	
	/**
	 * Imports from the original model
	 */
	«g.generateImports(model)»
	'''
	def generateWeightImports(PatternModel model)'''
	«FOR pattern : model.patterns»
	«FOR body : pattern.bodies»
	«FOR constraint : body.constraints»
	«IF constraint.ksKGateConstraint»
	import java «constraint.KGateWeightClass»
	«ENDIF»
	«ENDFOR»
	«ENDFOR»
	«ENDFOR»
	'''
	
	def getKGateWeightClass(Constraint constraint) {
		val body = constraint.eContainer as PatternBody
		val pattern = body.eContainer as Pattern
		val annotation = pattern.annotations.findFirst [ annotation |
			annotation.isWeightAnnotation(g.value(constraint.leftHandSide).toString)
		]
		return '''«(annotation.parameters.findFirst[p | "class".equals(p.name)].value as StringValue).value»'''
	}
	
	def generateInterfaceQueries()'''
	/**
	 * Interface queries for event management
	 */
	pattern unaryBETrace(element: EObject, name: EString, index: EInt, trace: UnaryTrace){
		UnaryTrace.source(trace, element);
		Trace.generator(trace, name);
		Trace.index(trace, index);
	}
	
	pattern unaryInsertion(element: EObject, name: java String, from: java Integer, to: java Integer, probability: java Double){
		find unaryBERequiredName(element, name, to, probability);
		neg find unaryBETrace(element, name, _, _);
		from == 0;
	} or {
		find unaryBERequiredName(element, name, to, probability);
		find unaryBETrace(element, name, _, _);
		from == max find unaryBETrace(element, name, #_, _);
		check(to > from);
	}
	
	pattern unaryRemoval(trace: UnaryTrace){
		UnaryTrace.source(trace, element);
		Trace.generator(trace, name);
		neg find unaryBERequiredName(element, name, _, _);
	} or {
		find unaryBERequiredName(element, name, multiplicity, _);
		UnaryTrace.source(trace, element);
		Trace.generator(trace, name);
		Trace.index(trace, idx);
		check(idx > multiplicity);
	}
	
	pattern unaryHandleOf(element: EObject, name: EString, handle: Handle){
		find unaryBETrace(element, name, _, trace);
		Trace.handle(trace, handle);
	}
	
	'''
	
	def generateUserQueries(PatternModel model)'''
	/**
	 * Stochastic patterns
	 */
	«createStochasticPatterns»
	
	/**
	 * Deterministic patterns
	 */
	«createDeterministicPatterns(model)»
	
	/**
	 * Event require pattern
	 */
	«createRequire(model)»
	
	'''
	
	var bodykgateidx = 1
	def createDeterministicPatterns(PatternModel model) {
		val patterns = newHashSet
		patterns.addAll(model.patterns)
		patterns.removeAll(stochasticPatterns)
		return '''
			«FOR pattern : patterns»
			«IF pattern.countKGateConstraints > 0»
			pattern «g.patternModifiers(pattern)» «g.patternName(pattern)»(«g.patternParameters(pattern)»){
				find wc_«g.patternName(pattern)»(«g.patternParameterNames(pattern)»,«FOR i : 1..pattern.countKGateConstraints SEPARATOR ","»_«ENDFOR»);
			}
			
			pattern wc_«g.patternName(pattern)»(«g.patternParameters(pattern)»,«FOR i : 1..pattern.countKGateConstraints SEPARATOR ","»c«i» : java MddHandleCollection«ENDFOR»)
			«FOR body : pattern.bodies SEPARATOR " or "»
			{
				«{bodykgateidx = 1
					null
				}»
				«FOR constraint : body.constraints»
				«IF constraint.ksKGateConstraint»
				«constraint.stochasticKGateConstraint(bodykgateidx++)»
				«ELSE»
				«g.constraint(constraint)»
				«ENDIF»
				«ENDFOR»
				«FOR i : (body.countKGateConstraints+1)..<(pattern.countKGateConstraints+1)»
				c«i» == eval(D.EmptyCollection);
				«ENDFOR»
			}
			«ENDFOR»
			«ELSE»
			«g.generateQuery(pattern)»
			«ENDIF»
			«ENDFOR»
		'''
	}
	
	def createStochasticPatterns() '''
		«FOR pattern : stochasticPatterns»
			«pattern.generateStochasticPattern»
		«ENDFOR»
	'''

	def generateStochasticPattern(Pattern pattern) '''
		pattern «g.patternName(pattern)»(«g.patternParameters(pattern)»,event: java MddHandle){
			event == OR find ua_«g.patternName(pattern)»(«g.patternParameterNames(pattern)»,#_);
		}
		private pattern ua_«g.patternName(pattern)»(«g.patternParameters(pattern)»,event: java MddHandle)
		«FOR body : pattern.bodies SEPARATOR " or "»
			{
				«body.generateStochasticBody»
			}
		«ENDFOR»
	'''
///////////////////////////////////////////////////////////////////////////////
	def generateNotPattern(Pattern pattern) '''
		private pattern not_«g.patternName(pattern)»(«g.patternParameters(pattern)», event: java MddHandle){
			neg find «g.patternName(pattern)»(«g.patternParameterNames(pattern)»,_);
			TraceModel.mddTrue(_,event);
		} or {
			find «g.patternName(pattern)»(«g.patternParameterNames(pattern)»,event1);
			event == eval(D.NOT(event1));
		}
	'''

	def dispatch stochasticVersion(Constraint constraint) '''«g.constraint(constraint)»'''

	def dispatch stochasticVersion(PatternCompositionConstraint constraint) '''
		«IF stochasticPatterns.contains((constraint.call as PatternCall).patternRef)»
			find «IF constraint.negative && constraint.notQueryNeeded»not_«ENDIF»«
		g.patternName((constraint.call as PatternCall).patternRef)»(«
		g.patternCallParameters((constraint.call as PatternCall))»,event«eventid++»);
		«ELSEIF (constraint.call as PatternCall).patternRef.isBasicEventDefinition»
			«IF constraint.negative»
				find unaryHandleOf(«g.patternCallParameters((constraint.call as PatternCall))»,"«
		g.patternName((constraint.call as PatternCall).patternRef)»",event«eventid++»_p);
				event_i_ == eval(D.NOT(«eventid++»_p));
			«ELSE»
				find unaryHandleOf(«g.patternCallParameters((constraint.call as PatternCall))»,"«
		g.patternName((constraint.call as PatternCall).patternRef)»",event«eventid++»);
			«ENDIF»
		«ELSE»
			«g.constraint(constraint)»
		«ENDIF»
	'''

	def dispatch stochasticVersion(CompareConstraint constraint) '''
«««		«IF constraint.KGateConstraint»
«««			«constraint.stochasticKGateConstraint»
«««		«ELSE»
			«g.constraint(constraint)»
«««		«ENDIF»
	'''
	
	/**
	 * Generate KGate equivalent
	 */
	private def stochasticKGateConstraint(Constraint constraint,int idx) '''
		collectionOf_«g.value(constraint.leftHandSide)» == COLLECT find «
			g.patternName(constraint.KGatePatternCall.patternRef)»(«
			g.patternCallParameters(constraint.KGatePatternCall)»,#_);
			//Cast: to avoid a false error indication in eclipse
		«g.value(constraint.leftHandSide)» == eval(D.WeightCollection(collectionOf_«g.value(constraint.leftHandSide)» as MddHandleCollection, «constraint.weightPredicateCall»));
	'''

	private def weightPredicateCall(Constraint constraint) {
		val body = constraint.eContainer as PatternBody
		val pattern = body.eContainer as Pattern
		val annotation = pattern.annotations.findFirst [ annotation |
			annotation.isWeightAnnotation(g.value(constraint.leftHandSide).toString)
		]
		return '''[cnt | «(annotation.parameters.findFirst[p | "class".equals(p.name)].value as StringValue).value».«
			(annotation.parameters.findFirst[p | "function".equals(p.name)].value as StringValue).value»(cnt)]'''
	}

/////////////////////////////////////////////////////////////////////////////////////////
	def boolean notQueryNeeded(Constraint constraint) {
		// TODO add to negated needed & generate
		return true
	}
	/**
	 * Generate body of stochastic query
	 */
	var eventid = 0

	def void eventidReset() { eventid = 0 }

	def generateStochasticBody(PatternBody body) '''
		«eventidReset»
		«FOR constraint : body.constraints»
			«constraint.stochasticVersion»
		«ENDFOR»
		«IF body.stochasticConstraintCount > 1»
			event==eval(D.AND(«FOR i : 0..(body.stochasticConstraintCount-1) SEPARATOR ","»event«i»«ENDFOR»));
		«ELSE»
			«IF body.stochasticConstraintCount < 1»
				TraceModel.mddTrue(_, event);
			«ELSE»
				event==event0;
			«ENDIF»
		«ENDIF»
	'''
//////////////////////////////////////////////////////////////////////////////////////////
	def stochasticPatternCall(PatternCall call, int id) {
		val constraint = call.eContainer as PatternCompositionConstraint
		if (constraint.negative) {
			'''[unimplemented Constraint Transformation]'''
		} else {
			'''find «g.patternName(call.patternRef)»(«g.patternCallParameters(call)»,event«id»);'''
		}

	}

	def createRequire(PatternModel model) '''
		pattern unaryBERequiredName(arg1:EObject, name: java String, index: java Integer, probability: java Double)
		«FOR required : model.patterns.filter[pattern | pattern.isBasicEventDefinition] SEPARATOR " or "»
			{
				find «g.patternName(required)»(«FOR i : 1..required.parameters.length SEPARATOR ","»arg«i»«ENDFOR»);
				name == "«g.patternName(required)»";
				probability == «required.probability»;
				index == 1;
			}
		«ENDFOR»
	'''
	/**
	 * KGate parameter list helper
	 */
	def countKGateConstraints(Pattern pattern){
		pattern.bodies.fold(0, [max, body | Math.max(max, body.countKGateConstraints)])
	}
	
	def countKGateConstraints(PatternBody body){
		body.constraints.fold(0, [sum, constraint | sum + (constraint.ksKGateConstraint ? 1 : 0)])
	}
	/**
	 * Constraint data access helpers
	 */
	 def getKGatePatternCall(Constraint constraint) {
		val assertion = constraint as CompareConstraint
		val aggregator = assertion.rightOperand as AggregatedValue
		return aggregator.call as PatternCall
	}

	def getLeftHandSide(Constraint constraint) {
		val assertion = constraint as CompareConstraint
		return assertion.leftOperand
	}
	
	/**
	 * Annotation data access helpers
	 */
	def double getProbability(Pattern pattern) {
		val value = pattern.annotations.findFirst[annotation|"BasicEvent".equals(annotation.name)].parameters.findFirst [ param |
			"probability".equals(param.name)
		].value as NumberValue
		Double.parseDouble(value.value.value)
	}
	
	/**
	 * Pattern classification methods
	 */
	def getStochasticPatterns(PatternModel model) {
		val patterns = new HashSet<Pattern>();
		val events = new HashSet<Pattern>();
		var changed = true
		while (changed) {
			changed = false
			for (pattern : model.patterns) {
				if (pattern.basicEventDefinition && !events.contains(pattern)) {
					events.add(pattern)
					changed = true
				}
				if ((pattern.refersFrom(patterns) && !patterns.contains(pattern)) ||
					(pattern.refersFrom(events) && !patterns.contains(pattern))) {
					patterns.add(pattern)
					changed = true
				}
			}
		}
		return patterns -> events
	}

	def refersFrom(Pattern pattern, Set<Pattern> patterns) {
		for (body : pattern.bodies) {
			for (constraint : body.constraints) {
				if (constraint instanceof PatternCompositionConstraint)
					if (patterns.contains((constraint.call as PatternCall).patternRef))
						return true
			}
		}
		false
	}
	
	/**
	 * Element identification methods
	 */
	def boolean isWeightAnnotation(Annotation annotation, String lhsname) {
		if(!"Weight".equals(annotation.name))
			return false
		val lhsparameter = annotation.parameters.findFirst [ param |
			"lhsname".equals(param.name)
		].value as StringValue
		return  lhsname.equals(lhsparameter.value)
	}
	
	def boolean ksKGateConstraint(Constraint constraint){
		if(constraint instanceof CompareConstraint)
			return  constraint.KGateConstraint
		return false
	}
	
	def boolean isKGateConstraint(CompareConstraint constraint) {
		val aggregator = constraint.rightOperand
		if (aggregator instanceof AggregatedValue) {
			return "KGate".equals(aggregator.aggregator.simpleName)
		}

		return false
	}
	
	def boolean isKGateBody(PatternBody body){
		for(constraint : body.constraints){
			if(constraint instanceof CompareConstraint){
				if(constraint.KGateConstraint)
					return true
			}
		}
		return false
	}
	
	def boolean isDeterministic(Constraint constraint) {
		// stochastic patterns must be in "find" (edge case: KGate) 
		if (constraint instanceof PatternCompositionConstraint) {
			if (stochasticPatterns.contains((constraint.call as PatternCall).patternRef) ||
				basicEventPatterns.contains((constraint.call as PatternCall).patternRef))
				return false
		}
		return true
	}
	
	def boolean isBasicEventDefinition(Pattern pattern) {
		pattern.annotations.exists[annotation|"BasicEvent".equals(annotation.name)]
	}

	def int stochasticConstraintCount(PatternBody body) {
		var count = 0
		for (constraint : body.constraints) {
			if (constraint instanceof PatternCompositionConstraint)
				if (stochasticPatterns.contains((constraint.call as PatternCall).patternRef) ||
					basicEventPatterns.contains((constraint.call as PatternCall).patternRef)) {
					count++
				}
		}
		return count
	}
	
	
}
 
