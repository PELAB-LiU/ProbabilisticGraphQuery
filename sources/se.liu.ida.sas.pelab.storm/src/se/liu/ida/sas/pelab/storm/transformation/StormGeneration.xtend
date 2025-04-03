package se.liu.ida.sas.pelab.storm.transformation

class StormGeneration {
	static def topEvent(String name){
		return '''toplevel "«name»";'''
	}
	static def basicEvent(String name, double probability){
		return '''"«name»" prob=«probability» dorm=1;'''
	}
	static def orGate(String name, String... inputs){
		return '''"«name»" or «FOR arg : inputs SEPARATOR " "»"«arg»"«ENDFOR»;'''
	}
	static def andGate(String name, String... inputs){
		return '''"«name»" and «FOR arg : inputs SEPARATOR " "»"«arg»"«ENDFOR»;'''
	}
	
	def static kof(Integer k, String name, String... inputs) {
		return '''"«name»" «k»of«inputs.length» «FOR arg : inputs SEPARATOR " "»"«arg»"«ENDFOR»;'''
	}
	
}