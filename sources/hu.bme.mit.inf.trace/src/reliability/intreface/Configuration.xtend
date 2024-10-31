package reliability.intreface

import org.eclipse.xtend.lib.annotations.Accessors

class Configuration {
	static var volatile boolean cancelled = false
	static def void abortIfCancelled(){
		if(cancelled)
			throw new CancellationException
	}
	static def boolean isCancelled(){cancelled}
	static def void cancel(){cancelled = true}
	static def void enable(){cancelled = false}
	
}

class CancellationException extends RuntimeException{
	new(){
		super("MDD operations are cancelled.")
	}
}