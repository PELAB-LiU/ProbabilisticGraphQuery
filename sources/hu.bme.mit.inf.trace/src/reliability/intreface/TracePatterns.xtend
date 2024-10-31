package reliability.intreface

class TracePatterns {
	static def traceImports()'''
//import epackage "http://www.example.org/tracemodel"
//import epackage "http://www.eclipse.org/emf/2002/Ecore"
import java hu.bme.mit.delta.mdd.MddHandle; 
'''
static def traceConnectivesReduced()'''
///////////////////////////////////
//////// Trace connectives ////////
///////////////////////////////////

pattern unaryBETrace(element: EObject, name: EString, index: EInt, trace: UnaryTrace){
	PartialInterpretation.reliabilityTrace(_, model);
	TraceModel.traces(model, trace);
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

pattern unaryUpdate(trace: UnaryTrace, probability: java Double){
	find unaryBERequiredName(element, name, multiplicity, probability);
	find unaryBETrace(element, name, index, trace);
	Trace.probability(trace, old);
	old != probability;
	check(multiplicity <= index);
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
static def traceConnectives()'''
«traceConnectivesReduced»

pattern minimumNewOf(name: EString, multiplicity: EInt){
	Scope.minNewElements(scp, multiplicity);
	Scope.targetTypeInterpretation(scp, pcti);
	PartialComplexTypeInterpretation(pcti);
	PartialComplexTypeInterpretation.interpretationOf(pcti, type);
	Type.name(type, name);
}

pattern maximumNewOf(name: EString, multiplicity: EInt){
	Scope.maxNewElements(scp, multiplicity);
	Scope.targetTypeInterpretation(scp, pcti);
	PartialComplexTypeInterpretation(pcti);
	PartialComplexTypeInterpretation.interpretationOf(pcti, type);
	Type.name(type, name);
}
'''	

static def getTimes(){
	#[/*D.totalcalctime, MddOrOperator.totalcalctime, MddCollectorOperator.totalcalctime*/]
} 
}




