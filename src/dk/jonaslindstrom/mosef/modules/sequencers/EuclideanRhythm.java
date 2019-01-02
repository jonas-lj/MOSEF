package dk.jonaslindstrom.mosef.modules.sequencers;

import java.util.ArrayList;
import java.util.List;

public class EuclideanRhythm {

  private int length;
  private int[] rhythm;

  public EuclideanRhythm(int beats, int length) {
    this.length = length;
    calculateEuclideanRhythm(beats, length);
  }

  private void calculateEuclideanRhythm(int n, int k) {

    List<Integer> remainder = new ArrayList<>();
    List<Integer> count = new ArrayList<>();

    int level = 0;
    int divisor = k - n;
    remainder.add(n);

    do {
      count.add(divisor / remainder.get(level));
      remainder.add(Math.floorMod(divisor, remainder.get(level)));
      divisor = remainder.get(level);
      level++;
    } while (remainder.get(level) > 1);

    count.add(divisor);

    List<Integer> pattern = new ArrayList<>();
    buildPattern(level, count, remainder, pattern);

    rhythm = new int[length];
    for (int i = 0; i < length; i++) {
      rhythm[i] = pattern.get(i);
    }
  }

  private void buildPattern(int level, List<Integer> count, List<Integer> remainder,
      List<Integer> pattern) {
    switch (level) {
      case -2:
        pattern.add(1);
        break;

      case -1:
        pattern.add(0);
        break;

      default:
        for (int i = 0; i < count.get(level); i++) {
          buildPattern(level - 1, count, remainder, pattern);
        }
        if (remainder.get(level) != 0) {
          buildPattern(level - 2, count, remainder, pattern);
        }

    }
  }


  public int[] getRhythm() {
    return this.rhythm;
  }

}
