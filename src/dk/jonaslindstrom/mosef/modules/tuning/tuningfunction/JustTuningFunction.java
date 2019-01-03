package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public class JustTuningFunction implements TuningFunction {

  double baseFrequency;
  int baseNote;

  // https://en.wikipedia.org/wiki/Five-limit_tuning
  double[] relative = new double[] {1.0f, 16.0f / 15.0f, 9.0f / 8.0f, 6.0f / 5.0f, 5.0f / 4.0f,
      4.0f / 3.0f, 7.0f / 5.0f, 3.0f / 2.0f, 8.0f / 5.0f, 5.0f / 3.0f, 16.0f / 9.0f, 15.0f / 8.0f};

  public JustTuningFunction() {
    this(0, 440.0f); // Concert pitch is default
  }

  public JustTuningFunction(int note, double frequency) {
    setBaseNote(note, frequency);
  }

  public void setBaseNote(int note, double frequency) {
    this.baseNote = note;
    this.baseFrequency = frequency;
  }

  @Override
  public double getFrequency(int note) {
    int relativeNote = note - baseNote;
    int tone = Math.floorMod(relativeNote, 12);
    int octave = Math.floorDiv(relativeNote, 12);
    double frequency = (double) (baseFrequency * Math.pow(2.0, octave) * relative[tone]);
    return frequency;
  }

}
