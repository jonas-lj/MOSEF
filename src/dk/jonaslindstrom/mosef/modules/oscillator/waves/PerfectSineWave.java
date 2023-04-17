package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import java.util.function.DoubleUnaryOperator;

/** To slow for real-time use. */
public class PerfectSineWave implements DoubleUnaryOperator {

  @Override
  public double applyAsDouble(double t) {
    return 0.99*Math.sin(2.0 * Math.PI * t);
  }
}
