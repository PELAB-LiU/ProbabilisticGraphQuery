package reliability.mdd;

import java.util.HashMap;

import hu.bme.mit.delta.mdd.MddVariable;
import reliability.mdd.ProbabilityMap;

@SuppressWarnings("serial")
public class ProbabilityMap extends HashMap<MddVariable, Double> {

	ProbabilityMap(ProbabilityMap map) {
	  super(map);
	}
	ProbabilityMap() {}
}
