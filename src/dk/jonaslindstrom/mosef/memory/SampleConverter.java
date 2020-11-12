package dk.jonaslindstrom.mosef.memory;

import org.apache.commons.math3.util.FastMath;

public class SampleConverter {

  private final int byteRate;
  private final double scale;
  private final boolean bigEndian;

  public SampleConverter(int bitRate, boolean bigEndian) {
    System.out.println(bitRate);

    this.byteRate = bitRate / 8;
    this.scale = FastMath.pow(2, -bitRate + 1);
    this.bigEndian = bigEndian;
  }

  public void fillSamples(double[] samples, byte[] bytes) {

    for (int i = 0; i < samples.length; i++) {
      int s = 0;

      if (bigEndian) {
        // TODO: Untested. Does it work for 24 bit?
        for (int j = 0; j < byteRate - 1; j++) {
          s |= ((bytes[byteRate * i + j]) << ((byteRate - j - 1) * 8)) & 0xFF;
        }
        s |= bytes[byteRate * i + byteRate - 1] & 0xff;

      } else {
        for (int j = 1; j < byteRate; j++) {
          s |= (bytes[byteRate * i + j]) << (j * 8);
        }
        s |= bytes[byteRate * i] & 0xff;
      }

      samples[i] = s * scale;
    }

  }

}
