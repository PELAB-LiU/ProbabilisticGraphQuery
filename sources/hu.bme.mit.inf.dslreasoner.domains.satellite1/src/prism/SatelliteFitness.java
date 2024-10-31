package prism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import hu.bme.mit.inf.measurement.utilities.Config;
import satellite1.CommSubsystem;
import satellite1.CubeSat3U;
import satellite1.CubeSat6U;
import satellite1.GroundStationNetwork;
import satellite1.InterferometryMission;
import satellite1.KaCommSubsystem;
import satellite1.Payload;
import satellite1.SmallSat;
import satellite1.Spacecraft;
import satellite1.UHFCommSubsystem;
import satellite1.XCommSubsystem;

@Deprecated
public class SatelliteFitness {
	   public static double maximal = 0;

	   public static Double evaluate(final InterferometryMission mission,Config cfg) {
		   Timer timeout = Config.timeout(cfg.asInt("timeout"), ()->{
				   System.out.println("Timeout. Exiting system.");
				   Nailgun.terminate();
				   System.exit(0);
		   });
		   long startTime = System.nanoTime();
		   
		   GroundStationNetwork gns = mission.getGroundStationNetwork();
	       List<Spacecraft> sats = mission.getSpacecraft();
	       ArrayList<CommSubsystem> comms = new ArrayList<>();
	       for(Spacecraft sat : sats) {
	          comms.addAll(sat.getCommSubsystem());
	       }
	       ArrayList<Payload> payloads = new ArrayList<>();
	       for(Spacecraft sat : sats) {
	          if(sat.getPayload()!=null) {
	             payloads.add(sat.getPayload());
	          }
	       }
	       HashMap<Object, List<CommSubsystem>> owner = new HashMap<>();
	       for(Spacecraft sat : sats) {
	          owner.put(sat, sat.getCommSubsystem());
	       }
	       if(gns != null) {
	          owner.put(gns, gns.getCommSubsystem());
	       }
	       HashMap<Object, Object> target = new HashMap<>();
	       for(final CommSubsystem comm : comms) {
	          if(comm.getTarget()!=null) {
	             target.put(comm, comm.getTarget());
	          }
	       }
	       HashMap<Object, Object> fallback = new HashMap<>();
	       for(final CommSubsystem comm : comms) {
	          CommSubsystem flb = comm.getFallback();
	          if(flb != null) {
	             fallback.put(comm, flb);
	          }
	       }
	       ArrayList<Object> withpayload = new ArrayList<>();
	       for(Spacecraft sat : sats) {
	          if(sat.getPayload()!=null) {
	             withpayload.add(sat);
	          }
	       }
	       List<?> groundcom = gns==null? new ArrayList<>() : gns.getCommSubsystem();
	       
	       
	       HashMap<Object, Object> values = new HashMap<>();
	       for(final Spacecraft sat : sats) {
	          {
	             if(sat instanceof CubeSat3U) {
	                values.put(sat, "1/62");
	             }
	             if(sat instanceof CubeSat6U) {
	                values.put(sat, "1/66");
	             }
	             if(sat instanceof SmallSat) {
	                values.put(sat, "1/70");
	             }
	          }
	       }
	       for(final CommSubsystem comm : comms) {
	          {
	             if(comm instanceof XCommSubsystem) {
	                values.put(comm, "1/13");
	             }
	             if(comm instanceof KaCommSubsystem) {
	                values.put(comm, "1/10");
	             }
	             if(comm instanceof UHFCommSubsystem) {
	                values.put(comm, "1/12");
	             }
	          }
	       }
	       if(gns != null && !gns.getCommSubsystem().isEmpty()) {
	          comms.addAll(gns.getCommSubsystem());
	       }
	       long buildTime = System.nanoTime();
	       new ModelTrafo().generate("tmp", "raw-temp", sats, comms, owner, target, fallback, groundcom, values,
	             withpayload);
	       long trafoTime = System.nanoTime();
	       final Double covresult = Nailgun.INSTANCE.verify("tmp/raw-temp.prism", 1);
	       long verifTime = System.nanoTime();
	       
	       timeout.cancel();
	       
	       System.out.println(cfg.asString("prefix")+";PRISM;"+cfg.asString("size")
	       		+";"+((trafoTime-startTime)/1000/1000)
	       		+";"+((verifTime-trafoTime)/1000/1000)
	       		+";"+((verifTime-startTime)/1000/1000)
	       		+";"+covresult
	       );
	       return covresult;
	   }

//	   public static Double fitnessOf(final InterferometryMission mission) {
//	      try {
//	         System.out.println("FITNESS EVALUATION");
//	         final Double result = SatelliteFitness.evaluate(mission,"");
//	         System.out.println("EVAL;PASS;" + System.nanoTime() + ";" + result + ";" + (result > maximal));
//	         if(result > maximal) {
//	            maximal = result;
//	         }
//	         return result;
//	      } catch(final Exception e) {
//	         System.out.println("EVAL;FAIL;" + System.nanoTime() + ";-1.0;false");
//	         return -1.0;
//	      }
//	   }
	}
