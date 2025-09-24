package reliability.mdd;

import java.util.HashMap;

import hu.bme.mit.delta.mdd.MddVariable;
import hu.bme.mit.delta.mdd.MddVariableHandle;
import reliability.mdd.ProbabilityMap;

public class ProbabilityMap extends HashMap<MddVariable, Double> {

	ProbabilityMap(ProbabilityMap map) {
	  super(map);
	}
	ProbabilityMap() {}
}
