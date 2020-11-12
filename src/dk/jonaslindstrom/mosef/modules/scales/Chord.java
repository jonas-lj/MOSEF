package dk.jonaslindstrom.mosef.modules.scales;

import dk.jonaslindstrom.mosef.modules.scales.ScaleFactory.Key;
import dk.jonaslindstrom.mosef.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Chord {

  public enum Quality {
    Minor, Major, Diminished, HalfDiminished, Augmented, Sus2, Sus4
  }

  public enum StackedIntervals {
    Six, Seven, Maj7, Nine, Eleven, Thirteen
  }

  public enum Alteration {
    Sharp, Flat
  }

  private final int root;
  private final Quality quality;
  private final StackedIntervals stackedIntervals;
  private final List<Pair<Alteration, Integer>> alterations;

  private final static String[] QUALITY = new String[] {"m", "", "dim", "ø", "+", "sus2", "sus4"};
  private final static String[] STACKED_INTERVALS = new String[] {"6", "7", "Δ", "9", "11", "13"};
  private final static String[] ALTERATION = new String[] {"♯", "♭"};

  private final static Map<Quality, Supplier<IntStream>> BASES = Map.of(
      Quality.Minor, () -> IntStream.of(0, 3, 7),
      Quality.Major, () -> IntStream.of(0, 4, 7),
      Quality.Diminished, () -> IntStream.of(0, 3, 6)
  );

  public Chord(int root, Quality quality, StackedIntervals stackedIntervals, List<Pair<Alteration, Integer>> alterations) {
    this.root = Math.floorMod(root, 12);
    this.quality = quality;
    this.stackedIntervals = stackedIntervals;
    this.alterations = alterations;
  }

  public Chord(int root, Quality quality, StackedIntervals stackedIntervals) {
    this(root, quality, stackedIntervals, null);
  }

  public Chord(int root, Quality quality) {
    this(root, quality, null, null);
  }

  public int getRoot() {
    return root;
  }

  public Quality getQuality() {
    return quality;
  }

  public int[] getNotes() {
    IntStream base = BASES.get(quality).get();

    // TODO alterations
    return base.map(n -> (n + root) % 12).toArray();
  }

  public StackedIntervals getStackedIntervals() {
    return stackedIntervals;
  }

  public List<Pair<Alteration, Integer>> getAlterations() {
    return alterations;
  }

  public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append(NoteName.noteAt(root));

    if (quality != null) {
      sb.append(QUALITY[quality.ordinal()]);
    }

    if (stackedIntervals != null) {
      sb.append(STACKED_INTERVALS[stackedIntervals.ordinal()]);
    }

    if (alterations != null) {
      for (Pair<Alteration, Integer> alteration : alterations) {
        sb.append(ALTERATION[alteration.first.ordinal()]);
        sb.append(alteration.second);
      }
    }
    return sb.toString();
  }

}
