package reliability.intreface;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;

import com.google.common.collect.Multiset;

import flight.AndInterfaceEvent;
import flight.WeightInterfaceEvent;
import hu.bme.mit.delta.mdd.MddHandle;
import reliability.cache.NoCacheManager;
import reliability.cache.ReliabilityCacheManager;
import reliability.cache.SessionCache;
import reliability.events.Event;
import reliability.events.delayed.OrderedDelayedAndEvent;
import reliability.mdd.MddHandleCollection;
import reliability.mdd.MddHandleMultiset;
import reliability.mdd.MddModel;
import reliability.mdd.ProbabilityMap;

public class D {
	/**
	 * Returns an empty MddHandle multiset 
	 * @return
	 */
	private static MddHandleCollection emptycollection = new MddHandleMultiset();
	@Pure
	public static MddHandleCollection EmptyCollection() {
		return emptycollection;
	}
	
	/**
	 * 
	 * @param lhs
	 * @param rhs
	 * @param rest
	 * @return
	 */
	@Pure
	public static Event AND(Event... rest) {
		boolean ignored = ExecutionTime.start();
		if(Configuration.isCancelled()){
			return null;
		}
		AndInterfaceEvent event = new AndInterfaceEvent();
		event.begin();
		
		Event result = new OrderedDelayedAndEvent(rest);
		ExecutionTime.stop(ignored);
		//MddHandle result = lhs.getHandle().intersection(rhs.getHandle());
		//for(Event event : rest) {
		//	MddHandle tmp =  result.intersection(event.getHandle());
		//	result = tmp;
		//}
		event.setArgc(rest.length);
		event.commit();
		return result;
	}
	
//	/**
//	 * 
//	 * @param lhs
//	 * @param rhs
//	 * @param rest
//	 * @return
//	 */
//	@Pure
//	public static MddHandle OR(MddHandle lhs, MddHandle rhs, MddHandle... rest) {
//		final double start = System.nanoTime();
//		MddHandle result = lhs.union(rhs);
//		for(MddHandle handle : rest) {
//			result = result.union(handle);
//		}
//		totalcalctime += (System.nanoTime() - start)/1000.0/1000;
//		return result;
//	}
	@Pure
	public static double WeightCollection(MddHandleCollection handles, final Function1<? super Integer, ? extends Double> weight, ProbabilityMap probabilities) {
		boolean ignored = ExecutionTime.start();
		if(Configuration.isCancelled()){
			return 0.0;
		}
		WeightInterfaceEvent event = new WeightInterfaceEvent();
		event.begin();
		double res = weight.apply(0);
		for(int i=1; i<=handles.getMultiset().size(); i++) {
			double w = weight.apply(i);
			if(w != 0) {
				double p = getProbablility(KOf(i, handles), MddModel.INSTANCE.getCacheForSession());
				res += (w * p);
			}
		}
		ExecutionTime.stop(ignored);
		event.commit();
		return res;
	}
	
	
	/**
	 * 
	 * @param k
	 * @param set
	 * @return
	 */
	@Pure
	private static MddHandle KOf(int k, MddHandleCollection in) {
//		final double start = System.nanoTime();
//		MddHandle res = KOf(k, in.getMultiset().toArray(new MddHandle[in.getMultiset().size()]));
//		final double mid0 = System.nanoTime();
//		totalcalctime += (System.nanoTime() - start)/1000.0/1000;
//		final double mid = System.nanoTime();
		Set<MddHandle> elements = in.getMultiset().elementSet();
		MddHandle res2 = KOf(k, elements.toArray(new MddHandle[elements.size()]), in.getMultiset());
//		final double end = System.nanoTime();
//		System.out.println("Assert: "+ res.equals(res2));
//		System.out.println("Diff: "+ (end-mid)/(mid0-start));
		return res2;
	}

	/**
	 * 
	 * @param k
	 * @param array
	 * @return
	 */
	private static MddHandle KOf(int k, MddHandle[] array) {
		if(k<=0)//No need for any to be satisfied
			return MddModel.INSTANCE.getHandleOf(true);
		if(k>array.length)//Cannot be satisfied even when all is true
			return MddModel.INSTANCE.getHandleOf(false);
		if(k==array.length) {//All needs to be true to be satisfied
			MddHandle result = MddModel.INSTANCE.getHandleOf(true);
			for(MddHandle handle : array)
				result = result.intersection(handle);
			return result;
		}
		//From here: k < array.length (Some must be satisfied)
		MddHandle[] remaining = Arrays.copyOfRange(array, 1, array.length);
		
		MddHandle selected = array[0];
		MddHandle rest = KOf(k-1, remaining);
		MddHandle noselect = KOf(k, remaining);
		
		return noselect.union(selected.intersection(rest));
	}
	
	/**
	 * 
	 * @param k
	 * @param events Unique unused events
	 * @param eventset Event multiplicity 
	 * @return
	 */
	private static MddHandle KOf(int k, MddHandle[] events, Multiset<MddHandle> eventset) {
		if(k<=0)//No need for any to be satisfied
			return MddModel.INSTANCE.getHandleOf(true);
		if(events.length==0)//There are no events to satisfy the gate (k>=1)
			return MddModel.INSTANCE.getHandleOf(false);
		
		MddHandle[] remaining = Arrays.copyOfRange(events, 1, events.length);
		
		MddHandle selected = events[0];
		MddHandle rest = KOf(k-eventset.count(selected), remaining, eventset);
		MddHandle noselect = KOf(k, remaining, eventset);
		
		return noselect.union(selected.intersection(rest));
	}
	/**
	 * 
	 * @param k
	 * @param array
	 * @return
	 */
	private static MddHandle exactlyKOf(int k, MddHandle[] array) {
		MddHandle k0 = KOf(k, array);
		MddHandle k1 = KOf(k+1, array);
		return k0.minus(k1);
	}
	@Pure
	public static double getProbablility(MddHandle root, ReliabilityCacheManager cache) {
		boolean ignored = ExecutionTime.start();
		if(Configuration.isCancelled()){
			return 0;
		}
		double res = MddModel.INSTANCE.getValue(root, cache);
		ExecutionTime.stop(ignored);
		return res;
	}
}




