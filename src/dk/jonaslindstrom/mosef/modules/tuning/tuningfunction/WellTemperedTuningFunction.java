package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;


public class WellTemperedTuningFunction implements TuningFunction {

  private final double center;
  private final double notes;
  private final int centerKey;

  public WellTemperedTuningFunction(double center, int notes, int centerKey) {
    this.center = center;
    this.notes = notes;
    this.centerKey = centerKey;
  }

  public WellTemperedTuningFunction() {
    this(440, 12, 69);
  }

  @Override
  public double getFrequency(int note) {
    return center * Math.pow(2.0, (note - centerKey) / notes);
  }

}
