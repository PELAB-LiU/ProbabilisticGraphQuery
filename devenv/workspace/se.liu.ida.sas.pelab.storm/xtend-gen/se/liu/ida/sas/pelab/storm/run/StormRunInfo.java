package se.liu.ida.sas.pelab.storm.run;

import java.util.Map;

@SuppressWarnings("all")
public class StormRunInfo {
  public final double transformation_ms;

  public final double run_ms;

  public final Map<String, Double> results;

  public final boolean timeout;

  public StormRunInfo(final double trafo, final double run, final Map<String, Double> results, final boolean timeout) {
    this.transformation_ms = trafo;
    this.run_ms = run;
    this.results = results;
    this.timeout = timeout;
  }
}
