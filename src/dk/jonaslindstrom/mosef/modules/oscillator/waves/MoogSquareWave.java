package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import java.util.function.DoubleUnaryOperator;

public class MoogSquareWave implements DoubleUnaryOperator {

  private final double a, b;
  private final double x1;

  public MoogSquareWave(double a, double b) {
    this.x1 = 0.5 - (2 - a/2) / (b - a);
    this.a = a;
    this.b = b;
  }

  public MoogSquareWave() {
    this(3.0 / 8.0, 80);
  }

  @Override
  public double applyAsDouble(double t) {
    if (t >= 0.5) {
      return -applyAsDouble(t - 0.5);
    }

    if (t < x1) {
      return -1 + t * a;
    } else {
      return -1 + x1*a + (t-x1) * b;
    }
  }
}
