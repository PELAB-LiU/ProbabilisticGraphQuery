package hu.bme.mit.inf.measurement.utilities

import java.util.Map
import java.util.HashMap
import java.util.Queue

class CSVLog {
	private final String[] columns
	private final String separator
	private final Queue<Map<String,String>> logs;
	private final Map<String,Object> current;
	
	new(String[] columns, String separator){
		this.columns = columns
		this.separator = separator
		this.logs = newLinkedList
		this.current = newHashMap
	}
	
	def Object log(String key, Object object){
		println('''«key» --> «object.toString.stringify»''')
		return current.put(key, object)
	}
	def commit(){
		val entry = new HashMap()
		for(key : columns){
			val value = current.get(key);
			if(value !== null){
				entry.put(key, value.toString);
			}
		}
		logs.add(entry)
		current.clear
	}
	
	override String toString(){
		return  '''
		«FOR key : columns SEPARATOR separator»«key»«ENDFOR»
		«FOR line : logs »
		«FOR key : columns SEPARATOR separator»«line.get(key).stringify»«ENDFOR»
		«ENDFOR»
		'''.toString
	}
	def String stringify(String source){
		if(source === null){
			return ""
		}
		if(source.contains("\"")
			|| source.contains(separator)
		){
			return "\""+source
				.replaceAll("\"","\"\"")
				//.replaceAll("\n","\\\\n")
			+"\""
		}
		return source
	}
}