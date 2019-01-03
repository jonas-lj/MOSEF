package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.modules.Module;

public class PulseWave implements Wave {

  private Module pulseWidth;

  public PulseWave(Module pulseWidth) {
    this.pulseWidth = pulseWidth;
  }

  @Override
  public double[] getSamples(double[] t) {
    double[] widths = pulseWidth.getNextSamples();
    for (int i = 0; i < t.length; i++) {
      if (t[i] < widths[i]) {
        t[i] = 1.0f;
      } else {
        t[i] = -1.0f;
      }
    }
    return t;
  }

}
