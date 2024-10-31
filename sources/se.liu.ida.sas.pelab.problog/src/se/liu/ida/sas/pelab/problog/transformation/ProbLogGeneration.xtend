package se.liu.ida.sas.pelab.problog.transformation

class ProbLogGeneration {
	static def basicEvent(double probability,String type, String... names)'''
	«probability»::«type»«IF names.size>0 »(«FOR arg : names SEPARATOR ','»«arg»«ENDFOR»)«ENDIF».
	'''
	static def dfact(String type, String... names)'''
	«type»«IF names.size>0 »(«FOR arg : names SEPARATOR ','»«arg»«ENDFOR»)«ENDIF»'''
}