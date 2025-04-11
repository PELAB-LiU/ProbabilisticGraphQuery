package hu.bme.mit.inf.measurement.utilities.configuration;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class EarlyCancelMonitor {
  private static final Logger LOG4J = LoggerFactory.getLogger(EarlyCancelMonitor.class);

  private int count = 0;

  private int success = 0;

  private final double threshold;

  private final int minimum;

  private final CancellationThresholdMode mode;

  public EarlyCancelMonitor(final int minimum, final double threshold, final CancellationThresholdMode mode) {
    this.threshold = threshold;
    this.minimum = minimum;
    this.mode = mode;
  }

  public int success() {
    return this.success++;
  }

  public boolean test() {
    EarlyCancelMonitor.LOG4J.debug("Check {} of {} with minimum {}, threshold {} and mode {}", Integer.valueOf(this.success), Integer.valueOf(this.count), Integer.valueOf(this.minimum), Double.valueOf(this.threshold), this.mode);
    if ((this.count < this.minimum)) {
      return true;
    }
    if ((this.mode == CancellationThresholdMode.IF_ABOVE)) {
      return (this.success > (this.count * this.threshold));
    } else {
      return (this.success < (this.count * this.threshold));
    }
  }

  public boolean testAndStrat() {
    final boolean result = this.test();
    this.count++;
    return result;
  }

  public int conditionalRun(final Function0<Boolean> function) {
    int _xifexpression = (int) 0;
    boolean _test = this.test();
    if (_test) {
      int _xblockexpression = (int) 0;
      {
        EarlyCancelMonitor.LOG4J.debug("Run lambda");
        this.count++;
        int _xifexpression_1 = (int) 0;
        boolean _booleanValue = function.apply().booleanValue();
        if (_booleanValue) {
          _xifexpression_1 = this.success();
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
}
