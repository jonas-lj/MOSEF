package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

import org.apache.commons.math3.util.FastMath;

/**
 * Just tuning function, where frequencies as computed as just tuning using a given centerkey as base.
 */
public class JustTuningFunction implements TuningFunction {

  private final int centerKey;
  private final double centerFrequency;

  // https://en.wikipedia.org/wiki/Five-limit_tuning
  double[] relative = new double[]{1.0, 16.0 / 15.0, 9.0 / 8.0, 6.0 / 5.0, 5.0 / 4.0,
      4.0 / 3.0, 7.0 / 5.0, 3.0 / 2.0, 8.0 / 5.0, 5.0 / 3.0, 16.0 / 9.0, 15.0 / 8.0};

  public JustTuningFunction() {
    this(440.0, 69); // Concert pitch is default
  }

  public JustTuningFunction(double centerFrequency, int centerKey) {
    this.centerFrequency = centerFrequency;
    this.centerKey = centerKey;
  }

  @Override
  public double getFrequency(int note) {
    note = note - centerKey; // normalize to make 0 -> 440
    int tone = FastMath.floorMod(note, 12);
    int octave = FastMath.floorDiv(note, 12);
    return centerFrequency* FastMath.pow(2.0, octave) * relative[tone];
  }

}
