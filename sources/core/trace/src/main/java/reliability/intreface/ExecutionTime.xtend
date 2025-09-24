package reliability.intreface

class ExecutionTime {
	var static long time = 0
	var static boolean running = false;
	var static long start = 0;
	def static boolean start(){
		if(running){
			return true
		} else {
			start = System.nanoTime
			running = true
			return false
		}
	}
	def static stop(boolean ignore){
		if( running && (!ignore)){
			time += System.nanoTime - start
			running = false;
		}
	}
	def static long time(){
		return time
	}
	def static void reset(){
		time = 0
		running = false
	}
}