package dk.jonaslindstrom.mosef.modules.noise;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

/**
 * This module can create white noise using the {@link Math#random} method.
 *
 * @author jonas
 */
public class WhiteNoise extends SimpleModule {

  public WhiteNoise(MOSEFSettings settings) {
    super(settings);
  }

  @Override
  public double getNextSample(double[] inputs) {
    return (FastMath.random() * 2.0) - 1.0;
  }

}
