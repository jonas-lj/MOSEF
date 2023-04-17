package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.examples;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.OffsetTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative.FiveLimitTuning;
import org.apache.commons.math3.util.FastMath;

/**
 * Just tuning function, where frequencies as computed as just tuning using a given centerkey as
 * base.
 */
public class JustTuningFunction extends OffsetTuningFunction {

  public JustTuningFunction() {
    this(440.0, 69); // Concert pitch is default
  }

  public JustTuningFunction(double centerFrequency, int centerKey) {
    super(new FiveLimitTuning(), centerKey, centerFrequency);
  }

}
