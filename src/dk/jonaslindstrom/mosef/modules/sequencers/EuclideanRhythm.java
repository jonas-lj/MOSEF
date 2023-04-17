package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
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
      if (i * n + 1 >= m * k) {
        rhythm[i] = 1;
        m += 1;
      }
    }

    return rhythm;
  }

}
