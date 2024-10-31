package hu.bme.mit.inf.querytransformation.query

import org.eclipse.emf.ecore.EPackage
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup
import org.eclipse.viatra.query.patternlanguage.emf.specification.GenericEMFPatternPQuery
import org.eclipse.viatra.query.patternlanguage.emf.specification.GenericQuerySpecification
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.CheckConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.ClassType
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.EClassifierConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.EntityType
import org.eclipse.viatra.query.patternlanguage.emf.vql.FunctionEvaluationValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.JavaType
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.PathExpressionConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue
import org.eclipse.viatra.query.patternlanguage.emf.vql.ValueReference
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.xbase.XBinaryOperation
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.XMemberFeatureCall
import tracemodel.TracemodelPackage
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint
import org.eclipse.xtext.xbase.XNumberLiteral
import org.eclipse.xtext.xbase.XCastedExpression

class VQLParser {
	def test() {
		// Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE)
		EMFPatternLanguageStandaloneSetup.doSetup()

		// ViatraQueryEngineOptions.setSystemDefaultBackends(ReteBackendFactory.INSTANCE, ReteBackendFactory.INSTANCE,
		// LocalSearchEMFBackendFactory.INSTANCE);
		val parsed = PatternParserBuilder.instance.parse(query().toString)
		if (parsed.hasError || parsed.patterns.empty) {
			println("Input contains errors or empty.")
			parsed.errors.forEach[err|println(err)]
		} else {
			val patternmodel = parsed.patterns.get(0).eContainer as PatternModel
			
			println(patternmodel.generateImports())
			for (pattern : parsed.patterns) {
				println(pattern.generateQuery())
			}
		}

	}
	def generateImports(PatternModel model)'''
	«FOR packageImport : model.importPackages.packageImport»
	import "«packageImport.EPackage.nsURI»"
	«ENDFOR»
	
	«FOR javaImport : model.importPackages.importDeclarations»
	import java «javaImport.importedType.packageName».«javaImport.importedType.simpleName»
	«ENDFOR»
	'''
	/**
	 * Pattern definition generation
	 */
	def generateQuery(Pattern pattern) '''
		«pattern.patternModifiers» pattern «pattern.patternName»(«pattern.patternParameters»)
		«FOR body : pattern.bodies SEPARATOR " or "»
			{
				«body.patternBody»	
			}
		«ENDFOR»
	'''
	
	def patternModifiers(Pattern pattern)'''«IF pattern.modifiers.private»private «ENDIF»'''
	def patternName(Pattern pattern)'''«pattern.name»'''
	def patternParameters(Pattern pattern)'''«FOR param : pattern.parameters SEPARATOR ','»«param.name»:«(param.type as EntityType).paramtype»«ENDFOR»'''
	def patternParameterNames(Pattern pattern)'''«FOR param : pattern.parameters SEPARATOR ','»«param.name»«ENDFOR»'''
	def patternBody(PatternBody body)'''
	«FOR constraint : body.constraints»
	«constraint.constraint»
	«ENDFOR»
	'''
	
	/**
	 * Parameter types
	 * (patternParameters uses it)
	 */
	def dispatch paramtype(ClassType param)'''«param.classname.name»'''
	def dispatch paramtype(JavaType param)'''java «IF param.classRef.packageName.startsWith("java.")»^«ENDIF»«param.classRef.packageName».«param.classRef.simpleName»'''
	
	/**
	 * Pattern Call
	 */
	def patternCall(PatternCall call)'''find «call.patternRef.patternName»(«call.patternCallParameters»)'''
	def patternCallParameters(PatternCall call)'''«FOR param : call.parameters SEPARATOR ','»«param.value»«ENDFOR»'''
	
	/**
	 * Constraint generation
	 */
	def defaultConstraint(Constraint constraint)'''[unresolved Constraint]'''
	def dispatch constraint(Constraint constraint)'''«constraint.defaultConstraint»'''
	def dispatch constraint(PathExpressionConstraint constraint) '''
		«constraint.sourceType.classname.name».«constraint.edgeTypes.get(0).refname.name»(«constraint.src.value»,«constraint.dst.value»);
	'''

	def dispatch constraint(PatternCompositionConstraint constraint) '''
		«IF constraint.negative»neg «ENDIF»«(constraint.call as PatternCall).patternCall»;
	'''

	def dispatch constraint(CompareConstraint constraint) '''
		«constraint.leftOperand.value»«constraint.feature.literal»«constraint.rightOperand.value»;
	'''

	def dispatch constraint(EClassifierConstraint constraint) '''
		«(constraint.type as ClassType).classname.name»(«constraint.^var.^var»);
	'''

	def dispatch constraint(CheckConstraint constraint) '''
		check(«constraint.expression.resolve»);
	'''
	
	/**
	 * Value generation
	 */
	def dispatch value(VariableReference reference) '''«IF reference.aggregator»#«ENDIF»«reference.getVar»'''
	
	def dispatch value(AggregatedValue reference)'''«reference.aggregator.simpleName» «(reference.call as PatternCall).patternCall»'''
	
	def dispatch value(FunctionEvaluationValue reference)'''eval(«reference.expression.resolve»)'''

	def dispatch value(ValueReference reference) '''«reference.toString»'''
	
	def dispatch value(StringValue reference) '''"«reference.getValue»"'''
	
	def dispatch value(NumberValue reference) '''«reference.getValue.value»'''

	
	
	/**
	 * Expression generation
	 * (body of check and eval constraints)
	 */
	def defaultResolve(XExpression expression)'''[unresolved XExpression]'''
	def defaultResolve(XMemberFeatureCall expression)'''[unresolved XExpression]'''
	def dispatch resolve(XExpression expression)'''«expression.defaultResolve»'''
	def dispatch resolve(XBinaryOperation expression)'''«expression.leftOperand.resolve» «expression.feature.jvmresolve» «expression.rightOperand.resolve»'''
	def dispatch resolve(XFeatureCall expresstion)'''«expresstion.feature.jvmresolve»'''
	def dispatch resolve(XNumberLiteral expression)'''«expression.value»'''
	def dispatch resolve(XCastedExpression expression)'''(«expression.target.resolve» as «expression.type.type.identifier.replaceAll("^java","^java")»)'''
	def dispatch resolve(XMemberFeatureCall expression)'''
	«IF expression.explicitOperationCall»«expression.operationCall»
	«ELSEIF expression.typeLiteral»«expression.feature.identifier»
	«ELSE»«expression.defaultResolve»«ENDIF»
	'''
	
	def defaultOperationCall(XMemberFeatureCall expression)'''[unresolved OperationCall]'''
	def operationCall(XMemberFeatureCall expression)'''
	«IF expression.static»«expression.memberCallTarget.resolve».«expression.feature.jvmresolve»(«FOR arg : expression.memberCallArguments SEPARATOR ','»«arg.resolve»«ENDFOR»)
	«ELSE»«expression.defaultOperationCall»
	«ENDIF»'''
	
	
	/**
	 * JVM resolve
	 */
	def defaultJVMResolve(JvmIdentifiableElement element)'''[unresolved JVMResolve]'''
	def dispatch jvmresolve(JvmIdentifiableElement expression)'''«expression.defaultJVMResolve()»'''
	def dispatch jvmresolve(JvmFormalParameter expression)'''«expression.name»'''
	def dispatch jvmresolve(JvmOperation expression)'''
	«IF "operator_lessEqualsThan".equals(expression.simpleName)»<=
	«ELSEIF "operator_lessThan".equals(expression.simpleName)»<
	«ELSEIF "operator_greaterThan".equals(expression.simpleName)»>
	«ELSEIF "operator_greaterEqualsThan".equals(expression.simpleName)»>=
	«ELSEIF "operator_multiply".equals(expression.simpleName)»*
	«ELSEIF "operator_plus".equals(expression.simpleName)»+
	«ELSEIF "operator_minus".equals(expression.simpleName)»-
	«ELSE»«expression.simpleName»
	«ENDIF»'''
	def dispatch jvmresolve(JvmGenericType expression)'''«expression.simpleName»'''
	


	def query() '''
		import java hu.bme.mit.delta.mdd.MddHandle; 
		import "http://www.example.org/satellite1"
		import "http://www.eclipse.org/emf/2002/Ecore"
		import "http://www.example.org/tracemodel"
		//import "http://www.eclipse.org/emf/2002/Ecore"
		import java hu.bme.mit.delta.mdd.MddHandle; 
		import java reliability.intreface.D;
		import java hu.bme.mit.delta.mdd.MddHandle; 
		import java reliability.mdd.OR;
		import java reliability.mdd.COLLECT;
		
		pattern canTransmit(css: CommSubsystem, handle: java MddHandle){
			find link(css, _, _);
			handle == OR find link(css, _, #_);
		}
		
		pattern unaryUpdate(trace: UnaryTrace, probability: java Double){
			find unaryBERequiredName(element, name, multiplicity, probability);
			find unaryBETrace(element, name, index, trace);
			Trace.probability(trace, old);
			old != probability;
			check(multiplicity <= index);
		}
				
		pattern sat_online(sat: Spacecraft, handle: java MddHandle){
			Spacecraft.commSubsystem(sat, css);
			find canTransmit(css, lnk_handle);
			find unaryHandleOf(css, "component", _, css_handle);//Sat can only have one transmitting css thus no aggregation is needed
			find unaryHandleOf(sat, "component", _, sat_handle);
			handle == eval(D.AND(sat_handle, css_handle, lnk_handle));
		}
		
		pattern coverage(coverage: java Double){
			InterferometryMission(mission);
			collection == COLLECT find ip_sat(_, #_);
			coverage == eval(domain.Coverage.calculate(collection));
		}
		pattern ip_sat(sat: Spacecraft, handle: java MddHandle){
			Spacecraft.payload(sat, _);
			find sat_online(sat, handle);
		}
		
		private pattern groundcom(css: CommSubsystem){
			GroundStationNetwork(gsn);
			CommunicatingElement.commSubsystem(gsn, css);
		}
		
		pattern cssReady(css: CommSubsystem, handle: java MddHandle){
			find groundcom(css);
			TraceModel.mddTrue(_, handle);
		} or {
			Spacecraft.commSubsystem(sat, css);
			find sat_online(sat, sat_handle);
			find unaryHandleOf(css, "component", _, css_handle);
			handle == eval(D.AND(sat_handle, css_handle));
		}
		
		
		
		
		pattern link(from: CommSubsystem, to: CommSubsystem, handle: java MddHandle){
			CommSubsystem.target(from, to);
			find cssReady(to, handle);
		} or {
			CommSubsystem.fallback(from, to);
			find cssReady(to, handle);
		}
		
		
		pattern unaryBERequiredName(element: EObject, name: java String, multiplicity: java Integer, probability: java Double){
			CubeSat3U(element);
			name == "component";
			probability == 0.98400034407713;
			multiplicity == 1;
		} or {
			CubeSat6U(element);
			name == "component";
			multiplicity == 1;
			probability == 0.98496269152523;
		} or {
			SmallSat(element);
			name == "component";
			multiplicity == 1;
			probability == 0.98581584235241;
		} or {
			XCommSubsystem(element);
			name == "component";
			multiplicity == 1;
			probability == 0.92596107864232;
		} or {
			KaCommSubsystem(element);
			name == "component";
			multiplicity == 1;
			probability == 0.90483741803596; 
		} or {
			UHFCommSubsystem(element);
			name == "component";
			multiplicity == 1;
			probability == 0.92004441462932;
		}
		
		
		
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
		
		pattern unaryHandleOf(element: EObject, name: EString, index: EInt, handle: Handle){
			find unaryBETrace(element, name, index, trace);
			Trace.handle(trace, handle);
		} 
	'''
}
