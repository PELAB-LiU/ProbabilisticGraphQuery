package hu.bme.mit.inf.measurement.utilities

import java.util.HashMap
import java.util.Timer
import java.util.TimerTask

class Config extends HashMap<String, Param>{
	def asString(String key){
		val value = get(key)
		if(value===null)
			throw new IllegalArgumentException("hu.bme.mit.inf.measurement.utilities.Config.get("+key+") is null. Unable to cast to string.")
		value.asString
	}
	def asInt(String key){
		val value = get(key)
		if(value===null)
			throw new IllegalArgumentException("hu.bme.mit.inf.measurement.utilities.Config.get("+key+") is null. Unable to cast to int.")
		value.asInt
	}
	def asDouble(String key){
		val value = get(key)
		if(value===null)
			throw new IllegalArgumentException("hu.bme.mit.inf.measurement.utilities.Config.get("+key+") is null. Unable to cast to double.")
		value.asDouble
	}
	def asBoolean(String key){
		val value = get(key)
		if(value===null)
			throw new IllegalArgumentException("hu.bme.mit.inf.measurement.utilities.Config.get("+key+") is null. Unable to cast to boolean.")
		value.asBoolean
	}
	def boolean isDefined(String key){
		containsKey(key)
	}
	new(String[] args){
		for(String arg : args) {
			if(!arg.startsWith("#")){
				val values = arg.split("=",2);
				if(values.size===2){
					val param = new Param(values.get(0), values.get(1));
					put(param.key, param);
				} else {
					val param = new Param(values.get(0), "true")
					put(param.key, param)
				}
				
			}
		}
	}

	def static Timer kill(long sec) {
		val timer = new Timer(true);
		timer.schedule(new TimerTask(){
			override run() {
				System.exit(0)
			}},
			sec*1000);
		return timer;
	}
	
	def static timeout(long sec, ()=>void then) {
		val timer = new Timer(true);
		timer.schedule(new TimerTask(){
			override run() {
				then.apply
			}},
			sec*1000);
		return timer;
	}
	def static gc(long delay){
		System.gc()
		Thread.sleep(delay)
	}
	
}

class Param {
	val String key;
	val String value;
	new(String key, String value){
		this.key = key
		this.value = value
	}
	def getKey(){
		key
	}
	def asString(){
		value
	}
	def asInt(){
		Integer.parseInt(value)
	}
	def asDouble(){
		Double.parseDouble(value)
	}
	def asBoolean(){
		Boolean.parseBoolean(value)
	}
	override hashCode(){
		return key.hashCode
	}
	override equals(Object o){
		if(o instanceof Param){
			return key.equals(o.key) &&
				value.equals(o.value)
		}
		return false
	}
}