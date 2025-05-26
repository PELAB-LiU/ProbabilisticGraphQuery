package hu.bme.mit.inf.measurement.utilities

import java.util.Map
import java.util.HashMap
import java.util.Queue
import java.util.List
import java.util.Arrays
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicBoolean
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

class CSVLog {
	static val Logger LOG4J = LoggerFactory.getLogger(CSVLog)
	
	val String[] columns
	val String separator
	val Queue<Map<String,String>> logs;
	val Map<String,Object> current;
	val AtomicBoolean unsaved;
	val Thread backup;
	
	new(String[] columns, String separator){
		this.columns = columns
		this.separator = separator
		this.logs = newLinkedList
		this.current = newHashMap
		
		val host = this
		this.unsaved = new AtomicBoolean(false)
		this.backup = new Thread(){
			override run(){
				if(host.unsaved.getAndSet(false)){
					val format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss")
					
					val writer = new FileWriter('''csvlog-«format.format(new Date())»-«host.hashCode».backup.txt''')
					writer.write(host.toString)
					
				}
			}
		}
		
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
		this.unsaved.set(true)
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
		this.unsaved.set(false)
		return  '''
		«FOR key : columns SEPARATOR separator»«key»«ENDFOR»
		«FOR line : logs »
		«FOR key : columns SEPARATOR separator»«line.get(key).stringify»«ENDFOR»
		«ENDFOR»
		'''.toString
	}
	override void finalize(){
		Runtime.runtime.removeShutdownHook(this.backup)
		if(this.unsaved.get){
			backup.run	
		}
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