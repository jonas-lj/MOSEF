package dk.jonaslindstrom.mosef.modules.scales;

public class NoteName {

  private final static String[] NOTES = new String[]{"C", "C♯", "D", "E♭", "E", "F", "F♯", "G",
      "A♭", "A", "B♭", "B"};
  private final static String[] SHARPS = new String[]{"C", "C♯", "D", "D♯", "E", "F", "F♯", "G",
      "G♯", "A", "A♯", "B"};
  private final static String[] FLATS = new String[]{"C", "D♭", "D", "E♭", "E", "F", "G♭", "G",
      "A♭", "A", "B♭", "B"};

  public static String noteAt(int note) {
    return NOTES[Math.floorMod(note, 12)];
  }

  public static String noteAtSharp(int note) {
    return NOTES[Math.floorMod(note, 12)];
  }

  public static String noteAtFlat(int note) {
    return NOTES[Math.floorMod(note, 12)];
  }

}
