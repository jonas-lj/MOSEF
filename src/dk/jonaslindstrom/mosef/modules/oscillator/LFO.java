package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.function.DoubleUnaryOperator;

public class LFO extends SimpleModule {

  private final DoubleUnaryOperator wave;
  private final double delta;
  private double t;

  /**
   * Create a new oscillator.
   *
   * @param settings
   * @param wave     A wave function.
   */
  public LFO(MOSEFSettings settings, double frequency, DoubleUnaryOperator wave) {
    super(settings);
    this.wave = wave;
    this.delta = frequency / settings.getSampleRate();
    this.t = -delta; // To make sure phase starts at 0
  }

  @Override
  public double getNextSample(double[] inputs) {
    t += delta;
    while (t >= 1.0) {
      t -= 1.0;
    }
    return wave.applyAsDouble(t);
  }

}
