package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import java.util.function.DoubleUnaryOperator;

public class TriangleWave implements DoubleUnaryOperator {

  @Override
  public double applyAsDouble(double t) {
    if (t < 0.25f) {
      return 4.0f * t;
    } else if (t < 0.75f) {
      return 2.0f - 4.0f * t;
    } else {
      return -4.0f + 4.0f * t;
    }
  }
}
