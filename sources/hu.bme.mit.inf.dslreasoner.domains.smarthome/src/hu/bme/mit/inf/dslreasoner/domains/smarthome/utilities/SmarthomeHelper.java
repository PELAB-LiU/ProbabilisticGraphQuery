package hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities;

import org.eclipse.xtext.xbase.lib.Pure;

import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UReal;

public class SmarthomeHelper {
	@Pure
	public static boolean incrementable(UReal temp1, UReal time1, UReal temp2, UReal time2) {
		boolean res = incrementConfidence(temp1, time1, temp2, time2)>0.5;
		return res;
	}
	@Pure
	public static double incrementConfidence(UReal temp1, UReal time1, UReal temp2, UReal time2) {
		UBoolean matching = temp2.minus(temp1).ge(new UReal(2, 0)).and(
				time2.gt(time1)).and(
				time2.minus(time1).lt(new UReal(60,0)));
		//System.out.println(matching.getC());
		return matching.getC();
	}
	@Pure
	public static boolean after(UReal t1, UReal t2) {
		//The original after constraint is for some reason always true
		boolean after = t2.minus(t1).gt(new UReal(0,0)).getC()>0.5;//t2.gt(t1).getC()>0.5;
		boolean within = t2.minus(t1).lt(new UReal(5*60,0)).getC()>0.5;
		return after && within;
	}
	@Pure
	public static boolean within5m(UReal t1, UReal t2) {
		return t2.minus(t1).lt(new UReal(5*60,0)).getC()>0.5;
	}
	
	@Pure
	public static boolean gt5000(UReal co) {
		return co.gt(new UReal(5000,0)).getC()>0.5;
	}
	@Pure
	public static double gt5000Confidence(UReal co) {
		return co.gt(new UReal(5000,0)).getC();
	}
	@Pure
	public static boolean within5s(UReal t1, UReal t2) {
		return t2.minus(t1).lt(new UReal(5,0)).getC()>0.5;
	}
	@Pure
	public static double P(int i) {
		return i==1?1.0:0.0;
	}
}
