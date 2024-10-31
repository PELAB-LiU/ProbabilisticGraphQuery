package hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities;

import org.eclipse.xtext.xbase.lib.Pure;

import surveillance.Drone;
import surveillance.UnidentifiedObject;
import uncertaindatatypes.UBoolean;
import uncertaindatatypes.UReal;

public class SurveillanceHelper {
	@Pure
	public static Coordinate move(Coordinate from, UReal speed, UReal angle, int time) {
		//https://github.com/atenearesearchgroup/lintra/blob/a59afb22e41542d710a2e6a8bb8b53150e65327e/lintra.examples.drones/src/transformations/confidence/surveillance/ConfidenceSurveillance.java#L104
		UReal d = speed.mult(new UReal(time,0));
		UReal x = from.x.add(d.mult(angle.cos()));
		UReal y = from.y.add(d.mult(angle.sin()));
		return new Coordinate(x,y);
	}
	//may do: selectByConfidence(u.getShotIDs(), 0.95).size()==0)//not have been shot
	@Pure
	public static boolean spd30(UReal speed) {
		//https://github.com/atenearesearchgroup/lintra/blob/a59afb22e41542d710a2e6a8bb8b53150e65327e/lintra.examples.drones/src/transformations/confidence/surveillance/ConfidenceSurveillance.java#L67
		return speed.gt(new UReal(30,0)).getC()>0.5;
	}
	@Pure
	public static boolean dst1000(Coordinate from, Coordinate to) {
		//https://github.com/atenearesearchgroup/lintra/blob/a59afb22e41542d710a2e6a8bb8b53150e65327e/lintra.examples.drones/src/transformations/confidence/surveillance/ConfidenceSurveillance.java#L78
		return from.distance(to).lt(new UReal(1000.0,0)).getC()>0.5;
	}

	/**
	 * Confidence check may be implemented as query constraint
	 * 
	 */
	@Pure
	public static double shotProbability(Coordinate from, Coordinate to, UReal speed, Double confidence) {
//		double spdHit = (1-Math.tanh((speed.toReal()-60)/20))/2;
//		double dst = from.distance(to).toReal();
//		double dstHit = (1-Math.tanh((dst-850)/250))/2;
//		return dstHit*spdHit;

		//https://github.com/atenearesearchgroup/lintra/blob/a59afb22e41542d710a2e6a8bb8b53150e65327e/lintra.examples.drones/src/transformations/confidence/surveillance/ConfidenceSurveillance.java#L91
		//Confidence of the Unidentified Object is treated as an independent event
		UBoolean speedGreater30 = speed.gt(new UReal(30, 0));
		UBoolean distanceLower1000 = from.distance(to).lt(new UReal(1000,0));
		
		double matchingConfidence = 0.99*
				speedGreater30.and(distanceLower1000).getC();
		return matchingConfidence;
	}
	
	@Pure
	public static double P(int k) {
		return k==1 ? 1 : 0;
	}
}
