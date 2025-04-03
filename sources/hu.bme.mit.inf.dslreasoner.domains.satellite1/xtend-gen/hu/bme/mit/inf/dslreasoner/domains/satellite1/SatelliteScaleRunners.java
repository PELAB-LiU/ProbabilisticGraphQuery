package hu.bme.mit.inf.dslreasoner.domains.satellite1;

import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SatelliteConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner;
import java.util.Timer;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import problog.ProblogSatelliteUtil;
import reliability.intreface.CancellationException;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import satellite1.SatellitePackage;
import se.liu.ida.sas.pelab.satellite1.storm.StormSatelliteUtil;

@SuppressWarnings("all")
public class SatelliteScaleRunners extends ViatraScaleRunner<SatelliteConfiguration> implements StormSatelliteUtil, ProblogSatelliteUtil {
  private final SatelliteModelGenerator modelgen = new SatelliteModelGenerator();

  private SatelliteModelWrapper instance;

  public SatelliteScaleRunners(final SatelliteConfiguration cfg) {
    super(cfg, SatellitePackage.eINSTANCE);
  }

  @Override
  public void preRun(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getSize(), seed);
    this.engine.getModel().getContents().add(this.instance.mission);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.engine, "coverage");
  }

  @Override
  public void runViatra(final CSVLog log) {
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
      this.engine.enable();
      final long it0sync = this.engine.getMdd().unaryForAll(this.engine.getEngine());
      final double coverage = this.checkMatches("coverage", this.engine);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      boolean _isTainted = this.engine.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
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
  public void runProblog(final CSVLog log) {
    this.runProblog(this.cfg, this.instance, log);
  }

  @Override
  public void runStorm(final CSVLog log) {
    this.runStorm(this.cfg, this.instance, log);
  }
}
