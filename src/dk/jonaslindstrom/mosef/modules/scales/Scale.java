package dk.jonaslindstrom.mosef.modules.scales;

public interface Scale {

  /** Compute the MIDI note (0-128, chromatically from C0) of a given note in this scale. */
  int noteAt(int position);

  default Scale transpose(int semitones) {
    return new TransposedScale(this, semitones);
  }

}
