package dk.jonaslindstrom.mosef.util;

import org.apache.commons.math3.util.FastMath;

public class Utils {

  public static double ratioFromCents(double cents) {
    return FastMath.pow(2, cents / 1200);
  }

  /**
   * -1 - 220 Hz 0 - 440 Hz 1 - 880 Hz
   *
   * @param cv
   * @return
   */
  public static double cv2freq(double cv) {
    return 440.0 * FastMath.pow(2, cv);
  }
}
