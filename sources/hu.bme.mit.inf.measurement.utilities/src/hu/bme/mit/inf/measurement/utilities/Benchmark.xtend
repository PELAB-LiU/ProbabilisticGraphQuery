package hu.bme.mit.inf.measurement.utilities

class Benchmark {
	var static long time = 0;
	var static long start
	var static int depth
	def static start(){
		if(depth===0){
			start = System.nanoTime
		}
		depth++
	}
	static def stop(){
		depth--
		if(depth===0){
			val end = System.nanoTime
			time += (end-start)
		}
	}
	static def long getTime(){
		return time
	}
}