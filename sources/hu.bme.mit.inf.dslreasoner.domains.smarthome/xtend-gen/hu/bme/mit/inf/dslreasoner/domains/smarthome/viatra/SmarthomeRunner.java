package hu.bme.mit.inf.dslreasoner.domains.smarthome.viatra;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.problog.ProblogSmarthomeUtil;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModel;
import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeModelGenerator;
import hu.bme.mit.inf.measurement.utilities.CSVLog;
import hu.bme.mit.inf.measurement.utilities.Config;
import hu.bme.mit.inf.measurement.utilities.configuration.SmarthomeConfiguration;
import hu.bme.mit.inf.measurement.utilities.viatra.ViatraBaseRunner;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reliability.intreface.Configuration;
import reliability.intreface.ExecutionTime;
import se.liu.ida.sas.pelab.smarthome.storm.StormSmarthomeUtil;
import smarthome.SmarthomePackage;

@SuppressWarnings("all")
public class SmarthomeRunner extends ViatraBaseRunner<SmarthomeConfiguration> implements ViatraSmarthomeUtil, StormSmarthomeUtil, ProblogSmarthomeUtil {
  private static final Logger LOG4J = LoggerFactory.getLogger(SmarthomeRunner.class);

  private final SmarthomeModelGenerator modelgen = new SmarthomeModelGenerator();

  private SmarthomeModel instance;

  public SmarthomeRunner(final SmarthomeConfiguration cfg) {
    super(cfg, SmarthomePackage.eINSTANCE);
  }

  @Override
  public void initBatch() {
    super.initBatch();
    this.initializePatterns(this.batch, "callProbability");
  }

  @Override
  public void runBatch(final CSVLog log) {
    Configuration.enable();
    try {
      final Resource resource = this.batch.getModel();
      final EcoreUtil.Copier copier = new EcoreUtil.Copier();
      final EObject result = copier.copy(this.instance.model);
      copier.copyReferences();
      resource.getContents().add(result);
      final HashMap<EObject, String> index = CollectionLiterals.<EObject, String>newHashMap();
      final Consumer<Map.Entry<EObject, EObject>> _function = (Map.Entry<EObject, EObject> it) -> {
        index.put(it.getValue(), this.instance.idmap.get(it.getKey()));
        EObject _value = it.getValue();
        String _plus = ("Mapping " + _value);
        String _plus_1 = (_plus + " to ");
        String _get = this.instance.idmap.get(it.getKey());
        String _plus_2 = (_plus_1 + _get);
        InputOutput.<String>println(_plus_2);
      };
      copier.entrySet().forEach(_function);
      ExecutionTime.reset();
      final Procedure0 _function_1 = () -> {
        SmarthomeRunner.LOG4J.info("Batch Timeout");
        Configuration.cancel();
        this.batch.dispose();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function_1);
      boolean _isTainted = this.batch.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("standalone.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.batch.enable();
      final long it0sync = this.batch.getMdd().unaryForAll(this.batch.getEngine());
      final String coverage = this.getMatchesJSON(this.batch, index);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("standalone.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("standalone.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("standalone.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("standalone.result", coverage);
      SmarthomeRunner.LOG4J.info("Batch completed in {}ms", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("standalone.timeout", Boolean.valueOf(Configuration.isCancelled()));
    }
  }

  @Override
  public void setupInitialModel(final int seed) {
    this.instance = this.modelgen.make(this.cfg.getHomes(), this.cfg.getPersons(), this.cfg.getSize());
    this.incremental.getModel().getContents().add(this.instance.model);
  }

  @Override
  public void initIncremental() {
    super.initIncremental();
    this.initializePatterns(this.incremental, "callProbability");
  }

  @Override
  public void runIncremental(final CSVLog log) {
    Configuration.enable();
    try {
      ExecutionTime.reset();
      final Procedure0 _function = () -> {
        SmarthomeRunner.LOG4J.info("Incremental Timeout");
        Configuration.cancel();
        this.incremental.dispose();
      };
      final Timer timeout = Config.timeout(this.cfg.getTimeoutS(), _function);
      boolean _isTainted = this.incremental.getEngine().isTainted();
      boolean _not = (!_isTainted);
      log.log("incremental.healthy", Boolean.valueOf(_not));
      final long it0start = System.nanoTime();
      this.incremental.enable();
      final long it0sync = this.incremental.getMdd().unaryForAll(this.incremental.getEngine());
      final String coverage = this.getMatchesJSON(this.incremental, this.instance.idmap);
      final long it0end = System.nanoTime();
      timeout.cancel();
      final long it0prop = ExecutionTime.time();
      log.log("incremental.total[ms]", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
      log.log("incremental.sync[ms]", Double.valueOf(((it0sync / 1000.0) / 1000)));
      log.log("incremental.prop[ms]", Double.valueOf(((it0prop / 1000.0) / 1000)));
      log.log("incremental.result", coverage);
      SmarthomeRunner.LOG4J.info("Incremental completed in {}ms", Double.valueOf((((it0end - it0start) / 1000.0) / 1000)));
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    } finally {
      log.log("incremental.timeout", Boolean.valueOf(Configuration.isCancelled()));
    }
  }

  @Override
  public void applyIncrement() {
    this.modelgen.iterate(this.instance, 1);
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
