package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public class TriangleWave extends SimpleWave {

  public TriangleWave(MOSEFSettings settings) {
    super(settings);
  }

  @Override
  public double getSample(double t) {
    if (t < 0.25f) {
      return 4.0f * t;
    } else if (t < 0.75f) {
      return 2.0f - 4.0f * t;
    } else {
      return -4.0f + 4.0f * t;
    }
  }

}
