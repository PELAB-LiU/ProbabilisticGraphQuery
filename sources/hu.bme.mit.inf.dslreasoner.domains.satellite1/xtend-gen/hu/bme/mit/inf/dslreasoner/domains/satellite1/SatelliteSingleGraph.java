package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import problog.Generation;
import reliability.intreface.CancellationException;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import satellite1.InterferometryMission;
import satellite1.SatellitePackage;

@SuppressWarnings("all")
public class SatelliteSingleGraph extends ViatraBaseRunner<SatelliteConfiguration> {
  private final SatelliteModelGenerator modelgen = new SatelliteModelGenerator();

  private SatelliteModelWrapper instance;

  public SatelliteSingleGraph(final SatelliteConfiguration cfg) {
    super(cfg, SatellitePackage.eINSTANCE);
  }

  @Override
  public void initStandalone() {
    super.initStandalone();
    this.initializePatterns(this.standalone, "coverage");
  }

  @Override
  public void runStandalone(final CSVLog log) {
    Configuration.enable();
    try {
      final Resource resource = this.standaloneResourceSet.createResource(URI.createFileURI("tmp-domain-standalone.xmi"));
      resource.getContents().add(EcoreUtil.<InterferometryMission>copy(this.instance.mission));
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final long it0start = System.nanoTime();
      this.standalone.enableAndPropagate();
      final long it0sync = this.standaloneMDD.unaryForAll(this.standalone);
      final double coverage = this.checkMatches("coverage", this.parsed, this.standalone);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("standalone.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("standalone.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("standalone.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("standalone.result", Double.valueOf(coverage));
    } catch (final Throwable _t) {
      if (_t instanceof CancellationException) {
        InputOutput.<String>println("Cancellation caught.");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("standalone.timeout", Boolean.valueOf(Configuration.isCancelled()));
      InputOutput.<String>println("Finally block executed.");
    }
  }

  @Override
  public void preRun(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getSize(), seed);
    this.incrementalDomainResource.getContents().add(this.instance.mission);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.incremental, "coverage");
  }

  @Override
  public void runIncremental(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        InputOutput.<String>println("Run cancelled with timeout.");
        Configuration.cancel();
        log.log("timeout", Boolean.valueOf(true));
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final long it0start = System.nanoTime();
      this.incremental.enableAndPropagate();
      final long it0sync = this.incrementalMDD.unaryForAll(this.incremental);
      final double coverage = this.checkMatches("coverage", this.parsed, this.incremental);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("incremental.result", Double.valueOf(coverage));
    } catch (final Throwable _t) {
      if (_t instanceof CancellationException) {
        InputOutput.<String>println("Cancellation caught.");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
      InputOutput.<String>println("Finally block executed.");
    }
  }

  @Override
  public void applyIncrement() {
    this.modelgen.makeRandomChange(this.instance, 1);
  }

  @Override
  public void runProblog(final CSVLog log) {
    try {
      String _probLogFile = this.cfg.getProbLogFile();
      final File file = new File(_probLogFile);
      file.createNewFile();
      final FileWriter writer = new FileWriter(file);
      String _probLogFile_1 = this.cfg.getProbLogFile();
      final ProcessBuilder builder = new ProcessBuilder("problog", _probLogFile_1);
      final long start = System.nanoTime();
      final String plmodel = new Generation().generateFrom(this.instance.mission).toString();
      writer.write(plmodel);
      writer.flush();
      writer.close();
      final long trafo = System.nanoTime();
      final Process process = builder.start();
      final AtomicBoolean timeoutFlag = new AtomicBoolean();
      final Procedure0 _function = () -> {
        try {
          InputOutput.<String>println("Run cancelled with timeout.");
          timeoutFlag.set(true);
          String _string = Long.valueOf(process.pid()).toString();
          Runtime.getRuntime().exec(new String[] { "kill", "-9", _string });
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      final HashMap<String, Object> output = new HashMap<String, Object>();
      InputStream _inputStream = process.getInputStream();
      final Scanner io = new Scanner(_inputStream);
      final Procedure1<String> _function_1 = (String line) -> {
        InputOutput.<String>println(("Debug: " + line));
        final Matcher match = this.cfg.getProbLogPattern().matcher(line);
        boolean _find = match.find();
        if (_find) {
          output.put(match.group(1), Double.valueOf(Double.parseDouble(match.group(2))));
        }
      };
      IteratorExtensions.<String>forEach(io, _function_1);
      final long end = System.nanoTime();
      timeout.cancel();
      log.log("problog.total[ms]", Double.valueOf((((end - start) / 1000.0) / 1000)));
      log.log("problog.trafo[ms]", Double.valueOf((((trafo - start) / 1000.0) / 1000)));
      log.log("problog.evaluation[ms]", Double.valueOf((((end - trafo) / 1000.0) / 1000)));
      Object _xifexpression = null;
      boolean _isEmpty = output.values().isEmpty();
      if (_isEmpty) {
        _xifexpression = Integer.valueOf(0);
      } else {
        _xifexpression = ((Object[])Conversions.unwrapArray(output.values(), Object.class))[0];
      }
      log.log("problog.result", _xifexpression);
      log.log("problog.timeout", Boolean.valueOf(timeoutFlag.get()));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
