package dk.jonaslindstrom.mosef.modules.limiter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

public class Distortion extends SimpleModule {

  public Distortion(MOSEFSettings settings, Module input, Module distortion) {
    super(settings, input, distortion);
  }

  @Override
  public double getNextSample(double[] inputs) {
    double x = inputs[0];
    double d = inputs[1];
    return x + FastMath.atan(16.0f * d * x) / 4.0f;
  }

}
