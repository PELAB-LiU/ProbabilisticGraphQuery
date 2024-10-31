package reliability.mdd;

import flight.MddSynchronizationEvent;
import flight.MddSynchronizationInsertEvent;
import flight.MddSynchronizationRemoveEvent;
import flight.MddSynchronizationUpdateEvent;
import flight.MddTraverseCalculationEvent;
import flight.MddUpdateUpdateEvent;
import flight.MddVarableCreationEvent;
import hu.bme.mit.delta.java.mdd.JavaMddFactory;
import hu.bme.mit.delta.mdd.LatticeDefinition;
import hu.bme.mit.delta.mdd.MddBuilder;
import hu.bme.mit.delta.mdd.MddGraph;
import hu.bme.mit.delta.mdd.MddHandle;
import hu.bme.mit.delta.mdd.MddSignature;
import hu.bme.mit.delta.mdd.MddVariable;
import hu.bme.mit.delta.mdd.MddVariableDescriptor;
import hu.bme.mit.delta.mdd.MddVariableOrder;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;
import reliability.cache.NoCacheManager;
import reliability.cache.ReliabilityCacheEntry;
import reliability.cache.ReliabilityCacheManager;
import reliability.cache.SessionCache;
import reliability.events.SimpleEvent;
import reliability.intreface.CacheMode;
import tracemodel.Trace;
import tracemodel.Trace1;
import tracemodel.Trace2;
import tracemodel.TraceModel;
import tracemodel.TracemodelFactory;
import tracemodel.TracemodelPackage;

@SuppressWarnings("all")
public class MddModel {
  private static final Map<String, MddModel> INSTANCES = Collections.<String, MddModel>unmodifiableMap(CollectionLiterals.<String, MddModel>newHashMap(Pair.<String, MddModel>of("incremental", new MddModel()), Pair.<String, MddModel>of("standalone", new MddModel())));

  public static MddModel INSTANCE = null;

  public static void changeTo(final String target) {
    MddModel.INSTANCE = MddModel.INSTANCES.get(target);
  }

  public static MddModel getInstanceOf(final String target) {
    return MddModel.INSTANCES.get(target);
  }

  private final TraceModel model;

  private final Set<MddTerminalEntry> entries;

  private final Set<MddTerminalEntry> inactiveEntries;

  @Accessors({ AccessorType.PUBLIC_GETTER, AccessorType.PUBLIC_SETTER })
  private ReliabilityCacheManager cacheManager;

  private final Set<IQuerySpecification<? extends ViatraQueryMatcher>> insertionSpecification;

  private final Set<IQuerySpecification<? extends ViatraQueryMatcher>> updateSpecification;

  private final Set<IQuerySpecification<? extends ViatraQueryMatcher>> removeSpecification;

  public void registerSpecificationIfNeeded(final IQuerySpecification<? extends ViatraQueryMatcher> spc) {
    this.graph.getUniqueTableSize();
    boolean _matches = spc.getFullyQualifiedName().matches("^Insertion[1-9]\\d*$");
    if (_matches) {
      this.insertionSpecification.add(spc);
      return;
    }
    boolean _matches_1 = spc.getFullyQualifiedName().matches("^Removal[1-9]\\d*$");
    if (_matches_1) {
      this.removeSpecification.add(spc);
      return;
    }
    boolean _matches_2 = spc.getFullyQualifiedName().matches("^Update[1-9]\\d*$");
    if (_matches_2) {
      this.updateSpecification.add(spc);
      return;
    }
  }

  public void resetModel() {
    this.graph = JavaMddFactory.getDefault().<Boolean>createMddGraph(LatticeDefinition.forSets());
    this.order = JavaMddFactory.getDefault().createMddVariableOrder(this.graph);
    this.entries.clear();
    this.inactiveEntries.clear();
    ProbabilityMap _probabilityMap = new ProbabilityMap();
    this.model.setProbabilities(_probabilityMap);
    this.model.getTraces().clear();
    MddHandle _handleOf = this.getHandleOf(false);
    SimpleEvent _simpleEvent = new SimpleEvent(_handleOf);
    this.model.setMddFalse(_simpleEvent);
    MddHandle _handleOf_1 = this.getHandleOf(true);
    SimpleEvent _simpleEvent_1 = new SimpleEvent(_handleOf_1);
    this.model.setMddTrue(_simpleEvent_1);
  }

  private MddModel() {
    this.graph = JavaMddFactory.getDefault().<Boolean>createMddGraph(LatticeDefinition.forSets());
    this.order = JavaMddFactory.getDefault().createMddVariableOrder(this.graph);
    this.entries = CollectionLiterals.<MddTerminalEntry>newHashSet();
    this.inactiveEntries = CollectionLiterals.<MddTerminalEntry>newHashSet();
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    _extensionToFactoryMap.putIfAbsent("xmi", _xMIResourceFactoryImpl);
    EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE);
    final TracemodelFactory tracefactory = TracemodelFactory.eINSTANCE;
    this.model = tracefactory.createTraceModel();
    ProbabilityMap _probabilityMap = new ProbabilityMap();
    this.model.setProbabilities(_probabilityMap);
    MddHandle _handleOf = this.getHandleOf(false);
    SimpleEvent _simpleEvent = new SimpleEvent(_handleOf);
    this.model.setMddFalse(_simpleEvent);
    MddHandle _handleOf_1 = this.getHandleOf(true);
    SimpleEvent _simpleEvent_1 = new SimpleEvent(_handleOf_1);
    this.model.setMddTrue(_simpleEvent_1);
    this.insertionSpecification = CollectionLiterals.<IQuerySpecification<? extends ViatraQueryMatcher>>newHashSet();
    this.updateSpecification = CollectionLiterals.<IQuerySpecification<? extends ViatraQueryMatcher>>newHashSet();
    this.removeSpecification = CollectionLiterals.<IQuerySpecification<? extends ViatraQueryMatcher>>newHashSet();
  }

  private MddTerminalEntry createVariable(final double probability) {
    final MddVarableCreationEvent event = new MddVarableCreationEvent();
    event.begin();
    boolean _isEmpty = this.inactiveEntries.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final MddTerminalEntry entry = ((MddTerminalEntry[])Conversions.unwrapArray(this.inactiveEntries, MddTerminalEntry.class))[0];
      this.inactiveEntries.remove(entry);
      this.updateProbability(this.model, entry.getVariable(), probability);
      event.setRecycled(true);
      event.commit();
      return entry;
    }
    final MddVariable variable = this.order.createOnTop(MddVariableDescriptor.create(Integer.valueOf(this.order.size()), 2));
    final MddSignature signature = this.order.createSignatureFromVariables(List.<MddVariable>of(variable));
    final MddHandle handle = new MddBuilder<Boolean>(signature).build(new Integer[] { Integer.valueOf(1) }, Boolean.valueOf(true));
    final MddTerminalEntry terminal = new MddTerminalEntry(variable, handle);
    this.entries.add(terminal);
    this.updateProbability(this.model, variable, probability);
    event.commit();
    return terminal;
  }

  public void updateProbability(final TraceModel model, final MddVariable variable, final double probability) {
    final MddUpdateUpdateEvent event = new MddUpdateUpdateEvent();
    event.begin();
    ProbabilityMap _probabilities = model.getProbabilities();
    final ProbabilityMap newmap = new ProbabilityMap(_probabilities);
    newmap.put(variable, Double.valueOf(probability));
    model.setProbabilities(newmap);
    event.commit();
  }

  public MddHandle getHandleOf(final boolean value) {
    return this.graph.getHandleFor(Boolean.valueOf(value));
  }

  public long getTableSize() {
    return this.graph.getUniqueTableSize();
  }

  public double getValue(final MddHandle root, final ReliabilityCacheManager cache) {
    boolean _isTerminal = root.isTerminal();
    if (_isTerminal) {
      int _xifexpression = (int) 0;
      Object _data = root.getData();
      if ((((Boolean) _data)).booleanValue()) {
        _xifexpression = 1;
      } else {
        _xifexpression = 0;
      }
      return _xifexpression;
    }
    final MddTraverseCalculationEvent event = new MddTraverseCalculationEvent();
    event.begin();
    final ReliabilityCacheEntry cacheEntry = cache.getOrCreateNode(root, 2);
    boolean _isValid = cacheEntry.isValid();
    if (_isValid) {
      event.setCached(true);
      event.commit();
      return cacheEntry.getProbability();
    }
    double result = 0.0;
    final Double node = this.model.getProbabilities().get(root.getVariableHandle().getVariable().get());
    double _result = result;
    double _value = this.getValue(root.get(0).toLowestSignificantVariable(), cache);
    double _multiply = ((1 - (node).doubleValue()) * _value);
    result = (_result + _multiply);
    double _result_1 = result;
    double _value_1 = this.getValue(root.get(1).toLowestSignificantVariable(), cache);
    double _multiply_1 = ((node).doubleValue() * _value_1);
    result = (_result_1 + _multiply_1);
    cache.updateNode(root, result);
    event.commit();
    return result;
  }

  public TraceModel getTraceModel() {
    return this.model;
  }

  public long unaryForAll(final ViatraQueryEngine engine) {
    try {
      final long start = System.nanoTime();
      final MddSynchronizationEvent event = new MddSynchronizationEvent();
      event.begin();
      final Callable<Long> _function = () -> {
        long _xblockexpression = (long) 0;
        {
          this.removeUnaryForAll(engine);
          this.updateUnaryForAll(engine);
          this.insertUnaryForAll(engine);
          InputOutput.<String>println("UNARY");
          _xblockexpression = System.nanoTime();
        }
        return Long.valueOf(_xblockexpression);
      };
      final Long end = ((AdvancedViatraQueryEngine) engine).<Long>delayUpdatePropagation(_function);
      event.commit();
      return ((end).longValue() - start);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void removeUnaryForAll(final ViatraQueryEngine engine) {
    final MddSynchronizationRemoveEvent event = new MddSynchronizationRemoveEvent();
    event.begin();
    final HashSet<Trace> tracesToRemove = new HashSet<Trace>();
    for (final IQuerySpecification<? extends ViatraQueryMatcher> specification : this.removeSpecification) {
      {
        final ViatraQueryMatcher matcher = engine.getMatcher(specification);
        final Consumer<IPatternMatch> _function = (IPatternMatch match) -> {
          Object _get = match.get(0);
          final Trace trace = ((Trace) _get);
          tracesToRemove.add(trace);
          if (this.cacheManager!=null) {
            this.cacheManager.removeNode(trace.getEvent().getHandle());
          }
        };
        matcher.forEachMatch(_function);
        this.model.getTraces().removeAll(tracesToRemove);
        final Consumer<Trace> _function_1 = (Trace trace) -> {
          final Function1<MddTerminalEntry, Boolean> _function_2 = (MddTerminalEntry entry) -> {
            boolean _xblockexpression = false;
            {
              final MddHandle handle = entry.getHandle();
              final int hc = handle.hashCode();
              int _hashCode = trace.getEvent().hashCode();
              _xblockexpression = (hc == _hashCode);
            }
            return Boolean.valueOf(_xblockexpression);
          };
          final MddTerminalEntry te = IterableExtensions.<MddTerminalEntry>findFirst(this.entries, _function_2);
          this.inactiveEntries.add(te);
        };
        tracesToRemove.forEach(_function_1);
      }
    }
    int _size = tracesToRemove.size();
    String _plus = ("Removed: " + Integer.valueOf(_size));
    InputOutput.<String>println(_plus);
    event.setTraces(tracesToRemove.size());
    event.commit();
  }

  private void updateUnaryForAll(final ViatraQueryEngine engine) {
    final MddSynchronizationUpdateEvent event = new MddSynchronizationUpdateEvent();
    event.begin();
    final HashMap<Trace, Double> tracesToUpdate = new HashMap<Trace, Double>();
    for (final IQuerySpecification<? extends ViatraQueryMatcher> specification : this.updateSpecification) {
      {
        final ViatraQueryMatcher matcher = engine.getMatcher(specification);
        final Consumer<IPatternMatch> _function = (IPatternMatch match) -> {
          Object _get = match.get(0);
          final Trace trace = ((Trace) _get);
          Object _get_1 = match.get(1);
          final Double value = ((Double) _get_1);
          tracesToUpdate.put(trace, value);
        };
        matcher.forEachMatch(_function);
        final BiConsumer<Trace, Double> _function_1 = (Trace trace, Double value) -> {
          trace.setProbability((value).doubleValue());
          final MddVariable variable = trace.getEvent().getHandle().getVariableHandle().getVariable().get();
          this.updateProbability(this.model, variable, (value).doubleValue());
          if (this.cacheManager!=null) {
            this.cacheManager.updateVariable(variable);
          }
        };
        tracesToUpdate.forEach(_function_1);
      }
    }
    int _size = tracesToUpdate.size();
    String _plus = ("Updated: " + Integer.valueOf(_size));
    InputOutput.<String>println(_plus);
    event.setTraces(tracesToUpdate.size());
    event.commit();
  }

  private void insertUnaryForAll(final ViatraQueryEngine engine) {
    final MddSynchronizationInsertEvent event = new MddSynchronizationInsertEvent();
    event.begin();
    final TracemodelFactory factory = TracemodelFactory.eINSTANCE;
    final HashSet<Trace> newtraces = new HashSet<Trace>();
    for (final IQuerySpecification<? extends ViatraQueryMatcher> specification : this.insertionSpecification) {
      {
        final ViatraQueryMatcher matcher = engine.getMatcher(specification);
        final int arity = Integer.parseInt(specification.getFullyQualifiedName().replace("Insertion", ""));
        final Consumer<IPatternMatch> _function = (IPatternMatch match) -> {
          try {
            Object _get = match.get(arity);
            final String name = ((String) _get);
            Object _get_1 = match.get((arity + 1));
            final Integer from = ((Integer) _get_1);
            Object _get_2 = match.get((arity + 2));
            final Integer to = ((Integer) _get_2);
            Object _get_3 = match.get((arity + 3));
            final Double probability = ((Double) _get_3);
            IntegerRange _upTo = new IntegerRange(((from).intValue() + 1), (to).intValue());
            for (final Integer idx : _upTo) {
              {
                Trace trace = null;
                switch (arity) {
                  case 1:
                    trace = factory.createTrace1();
                    Object _get_4 = match.get(0);
                    ((Trace1) trace).setArg1(((EObject) _get_4));
                    break;
                  case 2:
                    trace = factory.createTrace2();
                    Object _get_5 = match.get(0);
                    ((Trace2) trace).setArg1(((EObject) _get_5));
                    Object _get_6 = match.get(1);
                    ((Trace2) trace).setArg2(((EObject) _get_6));
                    break;
                  default:
                    {
                      event.reflection();
                      final Method factoryMethod = TracemodelFactory.class.getMethod(("createTrace" + Integer.valueOf(arity)));
                      Object _invoke = factoryMethod.invoke(factory);
                      final Trace t = ((Trace) _invoke);
                      IntegerRange _upTo_1 = new IntegerRange(1, arity);
                      for (final Integer i : _upTo_1) {
                        {
                          final Method set = t.getClass().getMethod(("setArg" + i), EObject.class);
                          set.invoke(t, match.get(((i).intValue() - 1)));
                        }
                      }
                    }
                    break;
                }
                MddHandle _handle = this.createVariable((probability).doubleValue()).getHandle();
                SimpleEvent _simpleEvent = new SimpleEvent(_handle);
                trace.setEvent(_simpleEvent);
                trace.setGenerator(name);
                trace.setIndex((idx).intValue());
                trace.setProbability((probability).doubleValue());
                newtraces.add(trace);
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        };
        matcher.forEachMatch(_function);
      }
    }
    this.model.getTraces().addAll(newtraces);
    int _size = newtraces.size();
    String _plus = ("Insertions: " + Integer.valueOf(_size));
    InputOutput.<String>println(_plus);
    event.setTraces(newtraces.size());
    event.commit();
  }

  public void initializePatterns(final ViatraQueryEngine engine) {
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function = (IQuerySpecification<? extends ViatraQueryMatcher> it) -> {
      engine.getMatcher(it);
    };
    this.insertionSpecification.forEach(_function);
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_1 = (IQuerySpecification<? extends ViatraQueryMatcher> it) -> {
      engine.getMatcher(it);
    };
    this.updateSpecification.forEach(_function_1);
    final Consumer<IQuerySpecification<? extends ViatraQueryMatcher>> _function_2 = (IQuerySpecification<? extends ViatraQueryMatcher> it) -> {
      engine.getMatcher(it);
    };
    this.removeSpecification.forEach(_function_2);
  }

  private final SessionCache globalCache = new SessionCache();

  private CacheMode cacheMode = CacheMode.SESSION;

  public ReliabilityCacheManager getCacheForSession() {
    ReliabilityCacheManager _switchResult = null;
    final CacheMode cacheMode = this.cacheMode;
    if (cacheMode != null) {
      switch (cacheMode) {
        case SESSION:
          _switchResult = new SessionCache();
          break;
        case GLOBAL:
          _switchResult = this.globalCache;
          break;
        case NO:
          _switchResult = NoCacheManager.INSTANCE;
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }

  public void invalidateCache() {
    this.globalCache.invalidateNode(null);
  }

  private MddGraph<Boolean> graph;

  private MddVariableOrder order;

  @Pure
  public ReliabilityCacheManager getCacheManager() {
    return this.cacheManager;
  }

  public void setCacheManager(final ReliabilityCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }
}
