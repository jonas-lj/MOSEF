package dk.jonaslindstrom.mosef.modules.limiter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class WaveFolder extends SimpleModule {

  private final double gain;

  public WaveFolder(MOSEFSettings settings, Module input, double gain) {
    super(settings, input);
    this.gain = gain;
  }

  public WaveFolder(MOSEFSettings settings, Module input) {
    this(settings, input, 2.0);
  }

  private static double foldFromAbove(double input, double limit) {
    if (input <= limit) {
      return input;
    }
    double excess = input - limit;
    return limit - excess;
  }

  private static double foldFromBelow(double input, double limit) {
    if (input >= limit) {
      return input;
    }
    double excess = limit - input;
    return limit + excess;
  }

  @Override
  public double getNextSample(double[] inputs) {
    double in = inputs[0] * gain;
    if (in > 1.0) {
      in = foldFromAbove(in, 1.0);
      in = foldFromBelow(in, 0.5);
    } else if (in < -1.0) {
      in = foldFromBelow(in, -1.0);
      in = foldFromAbove(in, -0.5);
    }
    return in;
  }

}
