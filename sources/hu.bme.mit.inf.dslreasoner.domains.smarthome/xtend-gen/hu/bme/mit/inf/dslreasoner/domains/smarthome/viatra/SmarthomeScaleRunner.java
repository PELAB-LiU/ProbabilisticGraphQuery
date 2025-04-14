package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.ProblogSmarthomeUtil;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraScaleRunner;
import java.util.Timer;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import se.liu.ida.sas.pelab.smarthome.storm.StormSmarthomeUtil;
import smarthome.SmarthomePackage;

@SuppressWarnings("all")
public class SmarthomeScaleRunner extends ViatraScaleRunner<SmarthomeConfiguration> implements ViatraSmarthomeUtil, StormSmarthomeUtil, ProblogSmarthomeUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(SmarthomeScaleRunner.class);

  private final SmarthomeModelGenerator modelgen = new SmarthomeModelGenerator();

  private SmarthomeModel instance;

  public SmarthomeScaleRunner(final SmarthomeConfiguration cfg) {
    super(cfg, SmarthomePackage.eINSTANCE);
  }

  @Override
  public boolean preRun(final int seed) {
    boolean _xblockexpression = false;
    {
      this.instance = this.modelgen.make(this.cfg.getHomes(), this.cfg.getPersons(), this.cfg.getSize());
      _xblockexpression = this.engine.getModel().getContents().add(this.instance.model);
    }
    return _xblockexpression;
  }

  @Override
  public void initViatra() {
    super.initViatra();
    this.initializePatterns(this.engine, "callProbability");
  }

  @Override
  public boolean runViatra(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        SmarthomeScaleRunner.LOG4J.info("Viatra Timeout");
        Configuration.cancel();
        this.engine.dispose();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.engine.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.engine.enable();
      final long it0sync = this.engine.getMdd().unaryForAll(this.engine.getEngine());
      final String coverage = this.getMatchesJSON(this.engine, this.instance.idmap);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("incremental.result", coverage);
      SmarthomeScaleRunner.LOG4J.info("Viatra completed in {}ms", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      return Configuration.isCancelled();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        SmarthomeScaleRunner.LOG4J.warn("Exception logged: {}", e.getMessage());
        SmarthomeScaleRunner.LOG4J.debug("Exception logged: {}, exception: {}", e.getMessage(), e);
        return Configuration.isCancelled();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
    }
  }

  @Override
  public boolean runProblog(final CSVLog log) {
    return this.runProblog(this.cfg, this.instance, log);
  }

  @Override
  public boolean runStorm(final CSVLog log) {
    return this.runStorm(this.cfg, this.instance, log);
  }
}
