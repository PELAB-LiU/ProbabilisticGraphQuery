package hu.bme.mit.inf.measurement.utilities.viatra;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IMatchUpdateListener;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQueryGroup;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineLifecycleListener;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.api.ViatraQueryModelUpdateListener;
import org.eclipse.viatra.query.runtime.api.scope.IBaseIndex;
import org.eclipse.viatra.query.runtime.api.scope.QueryScope;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryBackend;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryBackendFactory;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryResultProvider;
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint;

public class SuspendedQueryEngine extends AdvancedViatraQueryEngine{
	private final AdvancedViatraQueryEngine engine;
	private final Field suspended;
	private final Field backends;
	private int suspendcount = 0;
	
	public static SuspendedQueryEngine create(QueryScope scope) throws NoSuchFieldException, SecurityException {
		return new SuspendedQueryEngine(scope);
	}
	private SuspendedQueryEngine(QueryScope scope) throws NoSuchFieldException, SecurityException {
		engine = AdvancedViatraQueryEngine.createUnmanagedEngine(scope);
		
		suspended = engine.getClass().getDeclaredField("delayMessageDelivery"); 
		suspended.setAccessible(true);
		
		backends = engine.getClass().getDeclaredField("queryBackends"); 
		backends.setAccessible(true);
    
	}
	
	public void suspend() {
		try {
			suspended.setBoolean(engine, true);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void enableAndPropagate() {
		try {
			suspended.setBoolean(engine, false);
			Map<IQueryBackendFactory, IQueryBackend> backendMap = (Map<IQueryBackendFactory, IQueryBackend>) backends.get(engine);
			for (IQueryBackend backend : backendMap.values()) {
                backend.flushUpdates();
            }
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public <V> V delayUpdatePropagation(Callable<V> callable) throws InvocationTargetException {
		// TODO Auto-generated method stub
		return engine.delayUpdatePropagation(callable);
	}
	
	@Override
	public void addLifecycleListener(ViatraQueryEngineLifecycleListener listener) {
		engine.addLifecycleListener(listener);
	}

	@Override
	public void removeLifecycleListener(ViatraQueryEngineLifecycleListener listener) {
		engine.removeLifecycleListener(listener);
	}

	@Override
	public void addModelUpdateListener(ViatraQueryModelUpdateListener listener) {
		engine.addModelUpdateListener(listener);
	}

	@Override
	public void removeModelUpdateListener(ViatraQueryModelUpdateListener listener) {
		engine.removeModelUpdateListener(listener);
	}

	@Override
	public <Match extends IPatternMatch> void addMatchUpdateListener(ViatraQueryMatcher<Match> matcher,
			IMatchUpdateListener<? super Match> listener, boolean fireNow) {
		engine.addMatchUpdateListener(matcher, listener, fireNow);
	}

	@Override
	public <Match extends IPatternMatch> void removeMatchUpdateListener(ViatraQueryMatcher<Match> matcher,
			IMatchUpdateListener<? super Match> listener) {
		engine.removeMatchUpdateListener(matcher, listener);
	}

	@Override
	public <Matcher extends ViatraQueryMatcher<? extends IPatternMatch>> Matcher getMatcher(
			IQuerySpecification<Matcher> querySpecification, QueryEvaluationHint optionalEvaluationHints) {
		return engine.getMatcher(querySpecification, optionalEvaluationHints);
	}

	@Override
	public void prepareGroup(IQueryGroup queryGroup, QueryEvaluationHint optionalEvaluationHints) {
		engine.prepareGroup(queryGroup, optionalEvaluationHints);
	}

	@Override
	public boolean isManaged() {
		return engine.isManaged();
	}

	@Override
	public boolean isTainted() {
		return engine.isTainted();
	}

	@Override
	public void wipe() {
		engine.wipe();
	}

	@Override
	public void dispose() {
		engine.dispose();
	}

	@Override
	public IQueryBackend getQueryBackend(IQueryBackendFactory iQueryBackendFactory) {
		return engine.getQueryBackend(iQueryBackendFactory);
	}

	@Override
	public <Matcher extends ViatraQueryMatcher<? extends IPatternMatch>> Matcher getExistingMatcher(
			IQuerySpecification<Matcher> querySpecification, QueryEvaluationHint optionalOverrideHints) {
		return engine.getExistingMatcher(querySpecification, optionalOverrideHints);
	}

	@Override
	public ViatraQueryEngineOptions getEngineOptions() {
		return engine.getEngineOptions();
	}

	@Override
	public IQueryResultProvider getResultProviderOfMatcher(ViatraQueryMatcher<? extends IPatternMatch> matcher) {
		return engine.getResultProviderOfMatcher(matcher);
	}

	

	@Override
	public boolean isUpdatePropagationDelayed() {
		return engine.isUpdatePropagationDelayed();
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return engine.isDisposed();
	}

	@Override
	public IBaseIndex getBaseIndex() {
		return engine.getBaseIndex();
	}

	@Override
	public <Matcher extends ViatraQueryMatcher<? extends IPatternMatch>> Matcher getMatcher(
			IQuerySpecification<Matcher> querySpecification) {
		return engine.getMatcher(querySpecification);
	}

	@Override
	public ViatraQueryMatcher<? extends IPatternMatch> getMatcher(String patternFQN) {
		return engine.getMatcher(patternFQN);
	}

	@Override
	public <Matcher extends ViatraQueryMatcher<? extends IPatternMatch>> Matcher getExistingMatcher(
			IQuerySpecification<Matcher> querySpecification) {
		return engine.getExistingMatcher(querySpecification);
	}

	@Override
	public Set<? extends ViatraQueryMatcher<? extends IPatternMatch>> getCurrentMatchers() {
		return engine.getCurrentMatchers();
	}

	@Override
	public QueryScope getScope() {
		return engine.getScope();
	}
}
