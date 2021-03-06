package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Offset extends SimpleModule {

  /**
   * This module performs an affine transformation on an input, so the output is <i>center + input *
   * amplitude</i>.
   *
   * @param settings
   * @param input
   * @param center
   * @param amplitude
   */
  public Offset(MOSEFSettings settings, Module input, Module center,
      Module amplitude) {
    super(settings, input, center, amplitude);
  }

  @Override
  public double getNextSample(double[] inputs) {
    double i = inputs[0];
    double c = inputs[1];
    double a = inputs[2];

    if (c + a * i <= 0) {
      System.out.println(i);
    }

    return c + a * i;
  }

}
