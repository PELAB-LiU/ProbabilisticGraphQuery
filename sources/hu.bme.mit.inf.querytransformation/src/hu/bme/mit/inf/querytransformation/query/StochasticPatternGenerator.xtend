package hu.bme.mit.inf.querytransformation.query

import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern
import java.util.Set
import tracemodel.TracemodelPackage
import org.eclipse.emf.ecore.EPackage
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.io.File
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue
import java.util.HashSet
import org.eclipse.viatra.query.patternlanguage.emf.vql.Annotation
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference
import org.eclipse.emf.common.util.EList

/**
 * Supported features
 * @BasicEvent Defines a basic event with the specified probability. Only EXACTLY one parameter queries are supported. 
 * @Weight Specifies the weight function for a KGate aggregator
 * @NoEvent Forces the query to deterministic domain. This overrides implied stochastic nature and the effect of the @BasicEvent annotation.
 */
class StochasticPatternGenerator extends TransformationUtilities{
	val Set<Pattern> stochasticPatterns = newHashSet
	val Set<Pattern> basicEventPatterns = newHashSet
	
	
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
            «generateInterfaceQueries(patternmodel)»
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
			parsed.errors.forEach[err|println("Source error:"+err)]
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
            «generateInterfaceQueries(patternmodel)»
            '''
            println(vqlcontent) 
            
            //val parsed2 = PatternParserBuilder.instance.parse(vqlcontent)
            //parsed2.errors.forEach[error | System.err.println(error)]
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
	import java reliability.mdd.NOT;
	import java reliability.intreface.D;
	import java hu.bme.mit.delta.mdd.MddHandle; 
	import java reliability.mdd.MddHandleCollection;
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
	«IF constraint.isKGateConstraint»
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
			val assertion = constraint as CompareConstraint
			annotation.isWeightAnnotation(g.value(assertion.leftOperand).toString)
		]
		return '''«(annotation.parameters.findFirst[p | "class".equals(p.name)].value as StringValue).value»'''
	}
	
	def generateInterfaceQueries(PatternModel model)'''
	/**
	 * Interface queries for event management
	 */
	«FOR argc : 1..model.requiredPatternMaxArgCount()»
	«IF model.countRequiredArg(argc)>0»
	pattern BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»:EObject«ENDFOR», name: EString, index: EInt, trace: Trace«argc»){
		«FOR i : 1..argc»
		Trace«argc».arg«i»(trace, arg«i»);
		«ENDFOR»
		Trace.generator(trace, name);
		Trace.index(trace, index);
	}
	
	pattern Insertion«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»:EObject«ENDFOR», name: java String, from: java Integer, to: java Integer, probability: java Double){
		find BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, to, probability);
		neg find BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, _, _);
		from == 0;
	} or {
		find BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, to, probability);
		find BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, _, _);
		from == max find BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, #_, _);
		check(to > from);
	}
	
	pattern Removal«argc»(trace: Trace«argc»){
		«FOR i : 1..argc»
		Trace«argc».arg«i»(trace, arg«i»);
		«ENDFOR»
		Trace.generator(trace, name);
		neg find BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, _, _);
	} or {
		find BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, multiplicity, _);
		«FOR i : 1..argc»
		Trace«argc».arg«i»(trace, arg«i»);
		«ENDFOR»
		Trace.generator(trace, name);
		Trace.index(trace, idx);
		check(idx > multiplicity);
	}
	
	pattern Update«argc»(trace: Trace«argc», probability: java Double){
		find BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, multiplicity, probability);
		find BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, index, trace);
		Trace.probability(trace, old);
		old != probability;
		check(index <= multiplicity);
	}
	
	pattern HandleOf«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»:EObject«ENDFOR», name: EString, event: Handle){
		find BETrace«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR», name, _, trace);
		Trace.event(trace, event);
	}
	«ENDIF»
	«ENDFOR»
	
	
	'''
	
	def generateUserQueries(PatternModel model)'''
	/**
	 * User defined patterns
	 */
	«FOR pattern : model.patterns»
	«createPattern(pattern)»
	«ENDFOR»
	
	/**
	 * Event require pattern
	 */
	«createRequire(model)»
	
	'''
	
	
	def createPattern(Pattern pattern){
		val stochastic = stochasticPatterns.contains(pattern)
		val noevent = pattern.isEventBlocked
		val mods = g.patternModifiers(pattern)
		val name = g.patternName(pattern)
		val dparams = g.patternParameters(pattern)
		val dparamname = g.patternParameterNames(pattern)
		return '''
		«IF stochastic»
		«mods» pattern «name»(«dparams»,event: java Event){
			find ua_«name»(«dparamname»,_);
			event == OR find ua_«name»(«dparamname»,#_);
		}
		private pattern ua_«name»(«dparams»,event: java Event)
		«ELSE»
		«mods» pattern «name»(«dparams»)
		«ENDIF»
		«FOR body : pattern.bodies SEPARATOR " or "»
		{
			«createBody(body, stochastic, noevent)»
		}
		«ENDFOR»
		'''
		
	}
	
	var eventidx = 0
	def createBody(PatternBody body, boolean stochastic, boolean noevent){
		val eventcount = stochastic ? body.stochasticConstraintCount : 0
		eventidx = 0
		return '''
		«FOR constraint : body.constraints»
		«createConstraint(constraint, eventcount<=1, noevent)»
		«ENDFOR»
		«IF stochastic»
			«IF eventcount == 0»
			TraceModel.mddTrue(_, event);
			«««eventcount==1 is encoded in createConstraint. (output is directly assigned to the event)
			«ELSEIF eventcount >= 2»
			event==eval(D.AND(«FOR i : 0..(eventcount-1) SEPARATOR ","»event«i»«ENDFOR»));
			«ENDIF»
		«ENDIF»
		'''
	}
	
	def createConstraint(Constraint constraint, boolean singleevent, boolean noevent){
		val type = constraint.type
		switch type.key {
			case DEFAULT: {
				if(constraint instanceof PatternCompositionConstraint){
					val pc = constraint.call as PatternCall
					if(stochasticPatterns.contains(pc.patternRef)){
						
					}
				}
				return g.constraint(constraint)
			}
			case KGATE: {
				val cc = constraint as CompareConstraint
				val av = cc.rightOperand as AggregatedValue
				val pc = av.call as PatternCall
				
				val variable = g.value(cc.leftOperand)
				val name = g.patternName(pc.patternRef)
				val params = g.patternCallParameters(pc)
				
				val body = constraint.eContainer as PatternBody
				val pattern = body.eContainer as Pattern
				val annotation = pattern.annotations.findFirst [ annotation |
					annotation.isWeightAnnotation(variable.toString)
				]
				val predicate =  '''[cnt | «
					(annotation.parameters.findFirst[p | "class".equals(p.name)].value as StringValue).value».«
					(annotation.parameters.findFirst[p | "function".equals(p.name)].value as StringValue).value»(cnt)]'''
				
				return '''
					«IF type.value»
					collectionOf_«variable» == COLLECT find use_«name»(«params»,#_);
					«ELSE»
					collectionOf_«variable» == COLLECT find «name»(«params»,#_);
					«ENDIF»
					TraceModel.probabilities(_,internal_probabilities);
					//Cast: to avoid a false error indication in eclipse
					«variable» == eval(D.WeightCollection(collectionOf_«variable» as MddHandleCollection, «predicate», internal_probabilities));
					'''
			}
			case CALL: {
				val pcc = constraint as PatternCompositionConstraint
				val pattern = (pcc.call as PatternCall).patternRef
				val name = g.patternName(pattern)
				val params = g.patternCallParameters(pcc.call as PatternCall)
				return '''
					«IF type.value»
					find use_«name»(«params»,«IF noevent»_«ELSE»event«IF !singleevent»«eventidx++»«ENDIF»«ENDIF»);
					«ELSE»
					find «name»(«params»,«IF noevent»_«ELSE»event«IF !singleevent»«eventidx++»«ENDIF»«ENDIF»);
					«ENDIF»
				'''
			}
			case NOT: {
				val pcc = constraint as PatternCompositionConstraint
				val pattern = (pcc.call as PatternCall).patternRef
				val name = g.patternName(pattern)
				val params = g.patternCallParameters(pcc.call as PatternCall)
				if(noevent)
					return '''
						«IF type.value»
						//Stochastic constraint is ignored (always match): 
						//event_idx == NOT find use_«name»(«params»,#_);
						«ELSE»
						//Stochastic constraint is ignored (always match):
						//event_idx == NOT find «name»(«params»,#_);
						«ENDIF»
					'''
				return '''
					«IF type.value»
					event«IF !singleevent»«eventidx++»«ENDIF» == NOT find use_«name»(«params»,#_);
					«ELSE»
					event«IF !singleevent»«eventidx++»«ENDIF» == NOT find «name»(«params»,#_);
					«ENDIF»
				'''
			}
			case STAGGREGATE: {
				val cc = constraint as CompareConstraint
				val av = cc.rightOperand as AggregatedValue
				val aggregator = av.aggregator.simpleName
				val pc = av.call as PatternCall
				val name = g.patternName(pc.patternRef)
				val params = g.patternCallParameters(pc)
				return '''
					«IF type.value»
					««« delegate call to the attotated pattern
					«g.value(cc.leftOperand)»==«aggregator» find «name»(«params»);
					«ELSE»
					«g.value(cc.leftOperand)»==«aggregator» find «name»(«params»,_);
					«ENDIF»
				'''
			}
		}
				
	}
	
	def createRequire(PatternModel model) '''
		«FOR argc : 1..model.requiredPatternMaxArgCount»
		«IF model.countRequiredArg(argc)>0»
		pattern BERequiredName«argc»(«FOR i: 1..argc SEPARATOR ", "»arg«i»:EObject«ENDFOR»,name: java String, index: java Integer, probability: java Double)
		{
			find BERequiredName«argc»_many(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR»,name, index, _);
			probability == max find BERequiredName«argc»_many(«FOR i: 1..argc SEPARATOR ", "»arg«i»«ENDFOR»,name, index, #_);
		}
		
		pattern BERequiredName«argc»_many(«FOR i: 1..argc SEPARATOR ", "»arg«i»:EObject«ENDFOR»,name: java String, index: java Integer, probability: java Double)
		«FOR required : model.filterRequiredArg(argc) SEPARATOR " or "»
		{
			«IF required.probabilitySourceType===PSource.ANNOTATION»
			find «g.patternName(required)»(«FOR i : 1..required.parameters.length SEPARATOR ","»arg«i»«ENDFOR»);
			probability == «required.probability»;
			«ELSE»
			 find «g.patternName(required)»(«required.patternParemeterOverwrite(required.probabilityVariable,"probability")»);
			«ENDIF»
			name == "«g.patternName(required)»";
			index == 1;
		}
		«ENDFOR»
		«ENDIF»
		«ENDFOR»
		
		«FOR required : model.patterns.filter[pattern | pattern.isBasicEventDefinition]»
		private pattern use_«g.patternName(required)»(«g.patternParameters(required)», event: java Event){
			//find «g.patternName(required)»(«g.patternParameterNames(required)»);
			find HandleOf«required.reducedParameterCount»(«required.patternReducedParameterNames»,"«g.patternName(required)»",event);
			«IF required.probabilitySourceType === PSource.PARAMETER»
			find BETrace«required.reducedParameterCount»(«required.patternReducedParameterNames»,"«g.patternName(required)»",1,trace);
			Trace.probability(trace,«required.probabilityVariable.variable.name»);
			«ENDIF»
			 
		}
		«ENDFOR»
	'''
	def patternParemeterOverwrite(Pattern call, VariableReference parameter, String replacement){
		var idx = 1
		return '''«FOR param : call.parameters SEPARATOR ","»«IF param.equals(parameter.variable)»«replacement»«ELSE»arg«idx++»«ENDIF»«ENDFOR»'''
	}
	def patternReducedParameterNames(Pattern pattern){
		val parameter = pattern.probabilitySourceType===PSource.PARAMETER ? pattern.probabilityVariable.variable : null
		return '''«FOR param : pattern.parameters.filter[p | !p.equals(parameter)] SEPARATOR ','»«param.name»«ENDFOR»'''
	}
	
	
	def type(Constraint constraint){
		if(constraint.deterministic){
			if(constraint.isKGateConstraint){
				val pcc = constraint as CompareConstraint 
				val av = pcc.rightOperand as AggregatedValue
				val pattern = (av.call as PatternCall).patternRef
				return ConstraintType.KGATE -> basicEventPatterns.contains(pattern)
			} else {
				if(constraint instanceof CompareConstraint){
					val rho = constraint.rightOperand 
					if(rho instanceof AggregatedValue){
						val pattern = (rho.call as PatternCall).patternRef
						if(stochasticPatterns.contains(pattern)){
							return ConstraintType.STAGGREGATE -> basicEventPatterns.contains(pattern)
						}
					} 
				}
				return ConstraintType.DEFAULT -> false
			}
		} else {
			val pcc = constraint as PatternCompositionConstraint 
			val pattern = (pcc.call as PatternCall).patternRef
			if(pcc.negative){
				return ConstraintType.NOT -> basicEventPatterns.contains(pattern)
			} else {
				return ConstraintType.CALL -> basicEventPatterns.contains(pattern)
			}
		}
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
				if(!pattern.eventBlocked){
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
	
	def boolean isDeterministic(Constraint constraint) {
		// stochastic patterns must be in "find" (edge case: KGate)
		// stochastic patterns may be referred in aggregators (but will not produce event)  
		if (constraint instanceof PatternCompositionConstraint) {
			if (stochasticPatterns.contains((constraint.call as PatternCall).patternRef) ||
				basicEventPatterns.contains((constraint.call as PatternCall).patternRef))
				return false
		}
		return true
	}
	
	private enum ConstraintType{
		//Deterministic types (no event output)
		DEFAULT,		//deterministicPattern
		KGATE,			//v == KGate find find myStochasticPattern...
		STAGGREGATE,	//v==count find myStochasticPattern...
		
		//Stochastic constraints (has event output)
		CALL,			//find myStochasticPattern...
		NOT				//neg find myStochasticPattern...
	}
	
}












