package dk.jonaslindstrom.mosef.util;

public class Utils {

  public static double ratioFromCents(double cents) {
    return Math.pow(2, cents / 1200);
  }

  /**
   * -1 - 220 Hz
   * 0 - 440 Hz
   * 1 - 880 Hz
   * @param cv
   * @return
   */
  public static double cv2freq(double cv) {
    return 440.0 *  Math.pow(2, cv);
  }
}
