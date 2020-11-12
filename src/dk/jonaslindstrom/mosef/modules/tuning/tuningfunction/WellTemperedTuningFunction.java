package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;


import org.apache.commons.math3.util.FastMath;

public class WellTemperedTuningFunction implements TuningFunction {

  private final double center;
  private final double notes;
  private final int centerKey;

  /** Create a well-tempered tuning function with the given parameters. It maps centerKey to centerFrequency
   * and evenly distributes all other notes */
  public WellTemperedTuningFunction(double centerFrequency, int notesPerOctave, int centerKey) {
    this.center = centerFrequency;
    this.notes = notesPerOctave;
    this.centerKey = centerKey;
  }

  public WellTemperedTuningFunction() {
    this(440, 12, 69);
  }

  @Override
  public double getFrequency(int note) {
    return center * FastMath.pow(2.0, (note - centerKey) / notes);
  }

}
