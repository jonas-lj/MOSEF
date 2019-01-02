package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;


public class WellTemperedTuningFunction implements TuningFunction {

  @Override
  public float getFrequency(int note) {
    return (float) (440f * Math.pow(2.0f, note / 12.0f));
  }

}
