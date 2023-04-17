package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public interface TuningFunction {

  /**
   * Return the frequency of the given note in this tuning. When applicable, we use MIDI
   * conventions, eg. A3 = 69.
   */
  double getFrequency(int note);

}
