package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import java.util.function.DoubleBinaryOperator;

public class PulseWave implements DoubleBinaryOperator {

  @Override
  public double applyAsDouble(double t, double w) {
    if (t < w) {
      return 0.99;
    } else {
      return -.99;
    }
  }
}
