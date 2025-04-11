package hu.bme.mit.inf.measurement.utilities

import java.util.Map
import java.util.HashMap
import java.util.Queue
import java.util.List
import java.util.Arrays
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class CSVLog {
	static val Logger LOG4J = LoggerFactory.getLogger(CSVLog)
	
	val String[] columns
	val String separator
	val Queue<Map<String,String>> logs;
	val Map<String,Object> current;
	
	new(String[] columns, String separator){
		this.columns = columns
		this.separator = separator
		this.logs = newLinkedList
		this.current = newHashMap
	}
	
	def Object log(String key, Object object){
		LOG4J.debug("CSVSET {} --> {}", key, object);
		//println('''CSVSET «key» --> «object.toString.stringify»''')
		
		if(!columns.contains(key)){
			LOG4J.warn("Unlogged value for key. {}", key)
		}
		return current.put(key, object)
	}
	def commit(){
		val entry = new HashMap()
		for(key : columns){
			val value = current.get(key);
			if(value !== null){
				entry.put(key, value.toString);
			} else {
				LOG4J.warn("Missing entry for key. {}", key)
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