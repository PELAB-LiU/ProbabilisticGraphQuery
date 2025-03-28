package hu.bme.mit.inf.measurement.utilities.viatra;

import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import reliability.mdd.MddModel;

@SuppressWarnings("all")
public class EngineConfig {
  private static List<EngineConfig> configs = CollectionLiterals.<EngineConfig>newLinkedList();

  private final String mddInstanceName;

  private final ResourceSet resourceSet;

  @Accessors(AccessorType.PUBLIC_GETTER)
  private final Resource model;

  @Accessors(AccessorType.PUBLIC_GETTER)
  private final SuspendedQueryEngine engine;

  @Accessors(AccessorType.PUBLIC_GETTER)
  private final PatternParsingResults parsed;

  @Accessors(AccessorType.PUBLIC_GETTER)
  private final MddModel mdd;

  public EngineConfig(final String queries, final String name) {
    try {
      this.mddInstanceName = name;
      ResourceSetImpl _resourceSetImpl = new ResourceSetImpl();
      this.resourceSet = _resourceSetImpl;
      int _hashCode = this.hashCode();
      String _plus = (("model-tmp-" + this.mddInstanceName) + Integer.valueOf(_hashCode));
      String _plus_1 = (_plus + ".xmi");
      this.model = this.resourceSet.createResource(URI.createFileURI(_plus_1));
      this.mdd = MddModel.getInstanceOf(this.mddInstanceName);
      MddModel.changeTo(this.mddInstanceName);
      this.mdd.resetModel();
      this.mdd.invalidateCache();
      this.parsed = PatternParserBuilder.instance().parse(queries);
      final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function = (IQuerySpecification<? extends ViatraQueryMatcher> specification) -> {
        this.mdd.registerSpecificationIfNeeded(specification);
      };
      this.parsed.getQuerySpecifications().forEach(_function);
      int _hashCode_1 = this.hashCode();
      String _plus_2 = (("trace-tmp-" + this.mddInstanceName) + Integer.valueOf(_hashCode_1));
      String _plus_3 = (_plus_2 + ".xmi");
      final Resource traceRes = this.resourceSet.createResource(URI.createFileURI(_plus_3));
      traceRes.getContents().add(this.mdd.getTraceModel());
      EMFScope _eMFScope = new EMFScope(this.resourceSet);
      this.engine = SuspendedQueryEngine.create(_eMFScope);
      this.mdd.initializePatterns(this.engine);
      this.engine.enableAndPropagate();
      this.engine.suspend();
      EngineConfig.configs.add(this);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public void acquire() {
    final Consumer<EngineConfig> _function = (EngineConfig cfg) -> {
      cfg.suspend();
    };
    EngineConfig.configs.forEach(_function);
    MddModel.changeTo(this.mddInstanceName);
  }

  public void suspend() {
    this.engine.suspend();
  }

  public void enable() {
    boolean _isTainted = this.engine.isTainted();
    if (_isTainted) {
      throw new IllegalStateException("Attempting to use tainted query engine.");
    }
    this.engine.enableAndPropagate();
  }

  public void dispose() {
    boolean _isDisposed = this.engine.isDisposed();
    boolean _not = (!_isDisposed);
    if (_not) {
      EngineConfig.configs.remove(this);
      this.engine.dispose();
    }
  }

  @Pure
  public Resource getModel() {
    return this.model;
  }

  @Pure
  public SuspendedQueryEngine getEngine() {
    return this.engine;
  }

  @Pure
  public PatternParsingResults getParsed() {
    return this.parsed;
  }

  @Pure
  public MddModel getMdd() {
    return this.mdd;
  }
}
