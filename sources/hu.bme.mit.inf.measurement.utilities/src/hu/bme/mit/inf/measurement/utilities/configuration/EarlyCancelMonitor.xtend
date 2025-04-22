package hu.bme.mit.inf.measurement.utilities.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.eclipse.xtext.xbase.lib.Functions.Function0

class EarlyCancelMonitor {
	static val Logger LOG4J = LoggerFactory.getLogger(EarlyCancelMonitor);
	
	var int count = 0
	var int success = 0
	val double threshold
	val int minimum 
	
	val CancellationThresholdMode mode
	
	new(int minimum, double threshold, CancellationThresholdMode mode){
		this.threshold = threshold
		this.minimum = minimum
		this.mode = mode
	}
	def success(){
		success++
	}
	def boolean test(){
		LOG4J.info("Check {} of {} with minimum {}, threshold {} and mode {}", success, count, minimum, threshold, mode)
		if(count < minimum){
			return true
		}
		val thresholdValue = (1.0 * success) / count
		switch(mode){
			case CancellationThresholdMode.IF_ABOVE: return ! (thresholdValue > threshold)
			case CancellationThresholdMode.IF_BELOW: return ! (thresholdValue < threshold)
		}
	}
	def boolean testAndStrat(){
		val result = test()
		count++
		return result
	}
	
	def conditionalRun(Function0<Boolean> function){
		if(test()){
			LOG4J.debug("Run lambda")
			count++
			function.apply.booleanValue ? success()
		}
	}
}

enum CancellationThresholdMode{
	IF_BELOW,
	IF_ABOVE
}