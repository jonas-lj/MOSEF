package dk.jonaslindstrom.mosef.modules.scales;

public class TransposedScale implements Scale {

  private final Scale base;
  private final int semitones;

  TransposedScale(Scale base, int semitones) {
    this.base = base;
    this.semitones = semitones;
  }

  @Override
  public int noteAt(int position) {
    return base.noteAt(position) + semitones;
  }
}
