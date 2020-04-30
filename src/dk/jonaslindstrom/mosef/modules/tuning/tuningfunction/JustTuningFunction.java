package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public class JustTuningFunction implements TuningFunction {

  double baseFrequency;

  // https://en.wikipedia.org/wiki/Five-limit_tuning
  double[] relative = new double[] {1.0, 16.0 / 15.0, 9.0 / 8.0, 6.0 / 5.0, 5.0 / 4.0,
      4.0 / 3.0, 7.0 / 5.0, 3.0 / 2.0, 8.0 / 5.0, 5.0 / 3.0, 16.0 / 9.0, 15.0 / 8.0};

  public JustTuningFunction() {
    this(440.0); // Concert pitch is default
  }

  public JustTuningFunction(double frequency) {
    this.baseFrequency = frequency;
  }

  @Override
  public double getFrequency(int note) {
    int tone = Math.floorMod(note, 12);
    int octave = Math.floorDiv(note, 12);
    double frequency = (double) (baseFrequency * Math.pow(2.0, octave) * relative[tone]);
    return frequency;
  }

}
