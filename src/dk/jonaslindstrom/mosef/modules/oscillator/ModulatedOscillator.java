package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.function.DoubleBinaryOperator;

public class ModulatedOscillator extends SimpleModule {

  private final DoubleBinaryOperator wave;
  private final double scale;
  private double t;

  public ModulatedOscillator(MOSEFSettings settings, Module frequency, Module modulator,
      DoubleBinaryOperator wave) {
    super(settings, frequency, modulator);
    this.wave = wave;
    this.scale = 1.0 / settings.getSampleRate();
    this.t = 0.0;
  }

  @Override
  public double getNextSample(double[] inputs) {
    t += inputs[0] * scale;
    while (t > 1.0) {
      t -= 1.0;
    }
    return wave.applyAsDouble(t, inputs[1]);
  }
}
