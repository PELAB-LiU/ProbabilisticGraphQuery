package hu.bme.mit.inf.dslreasoner.domains.satellite1.performability;

import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.Pure;

public class Performability {
	final static double[] data = new double[] {0.0, 
			calculate2(1), 
			calculate2(2) - calculate2(1),
			calculate2(3) - calculate2(2), 
			calculate2(4) - calculate2(3), 
			calculate2(5) - calculate2(4),
			calculate2(6) - calculate2(5), 
			calculate2(7) - calculate2(6), 
			calculate2(8) - calculate2(7),
			calculate2(9) - calculate2(8), 
			calculate2(10) - calculate2(9)};
	@Pure
	public static double calculate(int sats) {
		try {
			return data[sats];
		} catch (ArrayIndexOutOfBoundsException e) {
			return calculate2(sats) - calculate2(sats - 1);
		}
	}

	public static double calculate2(int sats) {
		double time = 1;
		if (time <= 0 || sats < 2)
			return 0.0;
		double base = (1 - (2.0 / sats));
		double expo = 1 + (9 / time);
		double lin = 0.05 * time / 3;
		return Math.pow(base, expo) + lin;
	}
	
	public static double P(int i) {
		return i==1 ? 1.0 : 0.0;
	}

}
