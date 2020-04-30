package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dk.jonaslindstrom.mosef.modules.Module;

public class EuclideanRhythm extends Rhythm {

  public EuclideanRhythm(MOSEFSettings settings, Module clock, int onsets, int beats) {
    super(settings, clock, calculateEuclideanRhythm(onsets, beats));
  }

  private static int[] calculateEuclideanRhythm(int n, int k) {
    if (k < 1) {
      throw new IllegalArgumentException("Number of beats must be positive");
    }

    if (n > k) {
      throw new IllegalArgumentException("Number of onsets cannot exceed the number of beats");
    }

    if (n < 0) {
      throw new IllegalArgumentException("Number of onsets must be non-negative");
    }

    int[] rhythm = new int[k];

    if (n == 0) {
      return rhythm;
    }

    rhythm[0] = 1;
    int m = 1;
    for (int i = 1; i < k; i++) {
      if (i * n+1 >= m*k) {
        rhythm[i] = 1;
        m += 1;
      }
    }

    return rhythm;
  }

  public static void main(String[] arguments) {
    System.out.println(Arrays.toString(calculateEuclideanRhythm(5, 13)));
  }

//  private static int[] calculateEuclideanRhythm(int n, int k) {
//    int[] rhythm = new int[k];
//
//    double delta = (double) n / k;
//
//    rhythm[0] = 1;
//    int m = 1;
//    for (int i = 1; i < k; i++) {
//      if (i * delta >= m) {
//        rhythm[i] = 1;
//        m++;
//      }
//    }
//
//    return rhythm;
//  }


//  private static int[] calculateEuclideanRhythm(int n, int k) {
//
//    List<Integer> remainder = new ArrayList<>();
//    List<Integer> count = new ArrayList<>();
//
//    int level = 0;
//    int divisor = k - n;
//    remainder.add(n);
//
//    do {
//      count.add(divisor / remainder.get(level));
//      remainder.add(Math.floorMod(divisor, remainder.get(level)));
//      divisor = remainder.get(level);
//      level++;
//    } while (remainder.get(level) > 1);
//
//    count.add(divisor);
//
//    List<Integer> pattern = new ArrayList<>();
//    buildPattern(level, count, remainder, pattern);
//
//    int[] rhythm = new int[k];
//    for (int i = 0; i < k; i++) {
//      rhythm[i] = pattern.get(i);
//    }
//
//    System.out.println(Arrays.toString(rhythm));
//
//    return rhythm;
//  }
//
//  private static  void buildPattern(int level, List<Integer> count, List<Integer> remainder,
//      List<Integer> pattern) {
//    switch (level) {
//      case -2:
//        pattern.add(1);
//        break;
//
//      case -1:
//        pattern.add(0);
//        break;
//
//      default:
//        for (int i = 0; i < count.get(level); i++) {
//          buildPattern(level - 1, count, remainder, pattern);
//        }
//        if (remainder.get(level) != 0) {
//          buildPattern(level - 2, count, remainder, pattern);
//        }
//
//    }
//  }

}
