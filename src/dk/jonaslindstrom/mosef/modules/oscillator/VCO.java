package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.function.DoubleUnaryOperator;

public class VCO extends SimpleModule {

  private final DoubleUnaryOperator wave;
  private final double scale;
  private double t;

  /**
   * Create a new oscillator.
   *
   * @param settings
   * @param frequency A module controlling the frequency of the oscillator.
   * @param wave      A wave function.
   */
  public VCO(MOSEFSettings settings, Module frequency, DoubleUnaryOperator wave) {
    super(settings, frequency);
    this.wave = wave;
    this.scale = 1.0 / settings.getSampleRate();
    this.t = 0.0;
  }

  @Override
  public double getNextSample(double[] inputs) {
    t += inputs[0] * scale;
    while (t >= 1.0) {
      t -= 1.0;
    }

    while (t < 0.0) {
      t += 1.0;
    }

    return wave.applyAsDouble(t);
  }

}
