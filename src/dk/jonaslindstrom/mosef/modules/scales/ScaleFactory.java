package dk.jonaslindstrom.mosef.modules.scales;

public class ScaleFactory {

  public static Scale minor(int base) {
    return new SimpleScale(Math.floorMod(base, 12), new int[]{0, 2, 3, 5, 7, 8, 10},
        NoteName.noteAt(base) + " minor");
  }

  public static Scale minor(Key base) {
    return minor(base.ordinal());
  }

  public static Scale harmonicMinor(int base) {
    return new SimpleScale(Math.floorMod(base, 12), new int[]{0, 2, 3, 5, 7, 8, 11},
        NoteName.noteAt(base) + " minor");
  }

  public static Scale harmonicMinor(Key base) {
    return harmonicMinor(base.ordinal());
  }

  public static Scale major(int base) {
    return new SimpleScale(Math.floorMod(base, 12), new int[]{0, 2, 4, 5, 7, 9, 11},
        NoteName.noteAt(base) + " major");
  }

  public static Scale major(Key base) {
    return major(base.ordinal());
  }

  public static Scale minorPentatonic(int root) {
    return new SimpleScale(root, new int[]{0, 3, 5, 7, 10},
        NoteName.noteAt(root) + " minor pentatonic");
  }

  public static Scale majorPentatonic(int root) {
    return new SimpleScale(root, new int[]{0, 2, 4, 7, 9},
        NoteName.noteAt(root) + " major pentatonic");
  }

  public static Scale wholeTone(int root) {
    return new SimpleScale(root, new int[]{0, 2, 4, 6, 8, 10},
        NoteName.noteAt(root) + " whole-tone");
  }

  public static Scale chromatic(int root) {
    return position -> root + position;
  }

  public enum Key {
    C, Csharp, D, Eflat, E, F, Fsharp, G, Aflat, A, Bflat, B
  }

}
