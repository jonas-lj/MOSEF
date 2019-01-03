package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public class SawWave extends SimpleWave {

  public SawWave(MOSEFSettings settings) {
    super(settings);
  }

  @Override
  public double getSample(double t) {
    if (t < 0.5f) {
      return 2.0f * t;
    } else {
      return 2.0f * t - 2.0f;
    }
  }
}
