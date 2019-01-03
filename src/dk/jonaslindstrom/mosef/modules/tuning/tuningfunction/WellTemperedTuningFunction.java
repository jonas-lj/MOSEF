package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;


public class WellTemperedTuningFunction implements TuningFunction {

  @Override
  public double getFrequency(int note) {
    return (double) (440f * Math.pow(2.0f, note / 12.0f));
  }

}
