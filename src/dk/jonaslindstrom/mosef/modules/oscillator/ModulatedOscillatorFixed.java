package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.function.DoubleBinaryOperator;

public class ModulatedOscillatorFixed extends SimpleModule {

  private final DoubleBinaryOperator wave;
  private final double delta;
  private double t;

  public ModulatedOscillatorFixed(MOSEFSettings settings, double frequency, Module modulator,
      DoubleBinaryOperator wave) {
    super(settings, modulator);
    this.wave = wave;
    this.delta = frequency / settings.getSampleRate();
    this.t = -delta;
  }

  @Override
  public double getNextSample(double[] inputs) {
    t += delta;
    while (t > 1.0) {
      t -= 1.0;
    }
    return wave.applyAsDouble(t, inputs[0]);
  }
}
