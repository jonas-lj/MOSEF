package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

import java.util.function.DoubleUnaryOperator;

public class SquareWave implements DoubleUnaryOperator {

  @Override
  public double applyAsDouble(double t) {
    if (t < 0.5) {
      return 1.0;
    } else {
      return -1.0;
    }
  }
}
