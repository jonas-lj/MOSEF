package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

import java.util.function.DoubleUnaryOperator;

public class SawWave implements DoubleUnaryOperator {

  @Override
  public double applyAsDouble(double t) {
    if (t < 0.5f) {
      return 2.0f * t;
    } else {
      return 2.0f * t - 2.0f;
    }
  }
}
