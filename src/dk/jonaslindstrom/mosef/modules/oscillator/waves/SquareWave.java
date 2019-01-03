package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public class SquareWave extends SimpleWave {

  public SquareWave(MOSEFSettings settings) {
    super(settings);
  }

  @Override
  public double getSample(double t) {
    if (t < 0.5f) {
      return .99f;
    } else {
      return -.99f;
    }
  }

}
