package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public abstract class SimpleWave implements Wave {

  private double[] buffer;

  public SimpleWave(MOSEFSettings settings) {
    this.buffer = new double[settings.getBufferSize()];
  }

  public abstract double getSample(double t);

  @Override
  public double[] getSamples(double[] t) {
    for (int i = 0; i < t.length; i++) {
      buffer[i] = getSample(t[i]);
    }
    return buffer;
  }


}
