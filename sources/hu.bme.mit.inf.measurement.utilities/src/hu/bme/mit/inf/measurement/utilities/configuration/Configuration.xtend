package hu.bme.mit.inf.measurement.utilities.configuration

import com.beust.jcommander.Parameter
import java.io.File
import com.beust.jcommander.converters.FileConverter
import com.beust.jcommander.JCommander
import java.util.List
import com.beust.jcommander.IStringConverter
import java.util.regex.Pattern
import java.io.OutputStream
import java.io.PrintStream
import java.io.FileOutputStream
import java.util.Arrays

class BaseConfiguration {
	
	@Parameter(names = "--case", description = "Case study to run.", required = true)
	var RunConfiguration casestudy
	def RunConfiguration getCase(){casestudy}
	
	@Parameter(names = "--vql", description = "VQL file to transform and evaluate.", required = true, converter=FileConverter)
	var File vqlfile;
	def File vql(){vqlfile}
	
	@Parameter(names = "--size", description = "Size of models to generate for evaluation.", required = true)
	var int size_;
	def int getSize(){size_}
	
	@Parameter(names = "--seed", description = "Seeds for the model. Use format '<from>..<to>'.", required = true, listConverter=IntRangeConverter)
	var List<Integer> seeds = emptyList;
	def List<Integer> seeds(){seeds}//seeds}
	
	@Parameter(names = "--timeout", description = "Timeout for each run in seconds.")
	var long timeout = 60*20;
	def long getTimeoutS(){timeout}
	
	@Parameter(names = "--prefix", description = "Prefix in csv.")
	var String prefix = "N/A"
	def String getPrefix(){prefix}
	
	@Parameter(names = "--minimum", description = "Minimum number of measurements before early abort.")
	var int minimum = 20
	def int getMinimum(){minimum}
	
	@Parameter(names = "--minrate", description = "Minimum success rate of analysis to continue.")
	var double rate = 0.1
	def double getRate(){rate}
	
	@Parameter(names = "--warmups", description = "Number of warmup runs before measurement. Use format '<from>..<to>'.", listConverter=IntRangeConverter)
	var List<Integer> warmups = (1..3).toList
	def List<Integer> getWarmups(){
		warmups
	}
	
	@Parameter(names = "--gctime", description = "Garbage collection time between measurements in seconds.")
	var int gctime = 10
	def int getGCTime(){gctime*1000}
	
	@Parameter(names = "--iterations", description = "Number of iterations (incremental changes) in a run.")
	var int iterations = 4
	def int getIterations(){iterations}
	
	@Parameter(names = "--header", description = "List of CSV columns to include in the output.")
	var List<String> columns = newArrayList("prefix","size","run","iteration"
		,"incremental.total[ms]","incremental.result","incremental.sync[ms]", "incremental.prop[ms]","incremental.timeout"
		,"standalone.total[ms]","standalone.result","standalone.sync[ms]", "standalone.prop[ms]","standalone.timeout"
		,"problog.total[ms]","problog.result","problog.trafo[ms]", "problog.evaluation[ms]","problog.timeout"
		,"storm.total[ms]","storm.result","storm.trafo[ms]","storm.evaluation[ms]","storm.timeout"
		,"incremental.healthy","standalone.healthy"
	)
	def List<String> getCSVcolumns(){columns}
	
	@Parameter(names = "--delimiter", description = "Separator in the output CSV file.")
	var String delimiter = ","
	def String getDelimiter(){delimiter}
	
	@Parameter(names = "--preferabort", description = "Abort execution on erroneous state.")
	var boolean abort = false
	def boolean isFavourAbort(){abort}
	
	@Parameter(names = "--plmodel", description = "Temporary file for problog model.")
	var String plfile = "tmp-problog.pl"
	def String getProbLogFile(){plfile}
	
	@Parameter(names = "--stormmodel", description = "Temporary file for storm model.")
	var String stormfile = "tmp-storm.dft"
	def String getStormFile(){stormfile}
	
	@Parameter(names = "--logto", description = "Specify where to print the CSV output from measurement runs. System.out and System.err will redirect to standard and error output respectively.", variableArity=true, converter=PrintStreamConcereter)
	var List<PrintStream> out = #[System.out]
	def PrintStream out(){
		return new TeePrintStream(out)
	}
	@Parameter(names = "--warmuplogto", description = "Specify where to print the CSV output from the warmup runs. System.out and System.err will redirect to standard and error output respectively.", variableArity=true, converter=PrintStreamConcereter)
	var List<PrintStream> out2 = #[System.out]
	def PrintStream outWarmup(){
		return new TeePrintStream(out2)
	}
	
	static def BaseConfiguration parse(String... args){
		
		val config = new BaseConfiguration
		val parser = JCommander.newBuilder()
  			.addObject(config)
  			.build()
  		if(args===null || args.length===0){
  			parser.programName = "Base configuration"
  			parser.usage
  			return null
  		}
  		parser.parse(args)
  		return config
	}
	
	
	
}
class SatelliteConfiguration extends BaseConfiguration{
	@Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter=RegexPatternConverter)
	var Pattern pattern = Pattern.compile("(expected)\\((\\d\\.\\d+)\\):")
	def Pattern getProbLogPattern(){pattern}
	
	static def SatelliteConfiguration parse(String... args){
		val config = new SatelliteConfiguration
		JCommander.newBuilder()
  			.addObject(config)
  			.build()
  			.parse(args)
  		return config
	}	
}
class SmarthomeConfiguration extends BaseConfiguration{
	@Parameter(names = "--homes", description = "Number of homes to generate.")
	var int homes = 10;
	def int getHomes(){homes}
	
	@Parameter(names = "--persons", description = "Number of persons to generate.")
	var int persons = 1;
	def int getPersons(){persons}
	
	@Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter=RegexPatternConverter)
	var Pattern pattern = Pattern.compile("callprobability\\((\\d+),([01]\\.\\d*)\\)")
	def Pattern getProbLogPattern(){pattern}
	
	static def SmarthomeConfiguration parse(String... args){
		val config = new SmarthomeConfiguration
		JCommander.newBuilder()
  			.addObject(config)
  			.build()
  			.parse(args)
  		return config
	}
}
class SurveillanceConfiguration extends BaseConfiguration{
	@Parameter(names = "--threshold", description = "Probability of removing an object in an iteration.")
	var double threshold = 0.1;
	def double getThreshold(){threshold}
	
	@Parameter(names = "--plpattern", description = "Result extraction regex for ProbLog", converter=RegexPatternConverter)
	var Pattern pattern = Pattern.compile("result\\((\\d+),([01]\\.\\d*)\\)")
	def Pattern getProbLogPattern(){pattern}
	
	static def SurveillanceConfiguration parse(String... args){
		val config = new SurveillanceConfiguration
		JCommander.newBuilder()
  			.addObject(config)
  			.build()
  			.parse(args)
  		return config
	}
}

class IntRangeConverter implements IStringConverter<List<Integer>>{
	override convert(String arg) {
		try{
		val bounds = arg.split("\\.\\.")
		val list = newArrayList
		list.addAll(Integer.parseInt(bounds.get(0))..Integer.parseInt(bounds.get(1)))
		return list
		
		} catch(IndexOutOfBoundsException | NumberFormatException e){
			return emptyList
		}
	}
	
}
class PrintStreamConcereter implements IStringConverter<OutputStream>{
	override convert(String arg) {
		switch(arg){
			case "System.out": return System.out
			case "System.err": return System.err
			default: return new PrintStream(new FileOutputStream(arg, true))
		}
	}
	
}
class RegexPatternConverter implements IStringConverter<Pattern>{
	override convert(String arg) {
		return Pattern.compile(arg)
	}	
}
enum RunConfiguration{
	SAT,
	SRV,
	SH
}

class TeePrintStream extends PrintStream {
    val List<PrintStream> out;
	
    new(PrintStream... out){
    	super(OutputStream.nullOutputStream)
    	this.out = Arrays.asList(out)
    }
    
    override boolean checkError(){
    	return out.fold(false, [accu, current | accu || current.checkError])
    }

    override void write(int x) {
        out.forEach[it.write(x)]
    }

    override void write(byte[] x, int o, int l) {
    	out.forEach[it.write(x, o, l)]
    }

    override void close() {
        out.forEach[it.close]
        super.close();
    }

    override void flush() {
        out.forEach[it.flush]
    }
}



