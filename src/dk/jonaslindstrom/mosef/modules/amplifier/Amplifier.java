package dk.jonaslindstrom.mosef.modules.amplifier;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * Instances of this class represents amplifier modules, where a signal form an input module is
 * multiplied with the value of another module.
 *
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 */
public class Amplifier extends SimpleModule {

  private final double level;

  /**
   * Create a new amplifier module taking an input module and a level module. The output will be the
   * product of the two modules.
   *
   * @param input The input module.
   * @param level The amplification level.
   */
  public Amplifier(MOSEFSettings settings, Module input, double level) {
    super(settings, input);
    this.level = level;
  }

  @Override
  public double getNextSample(double[] inputs) {
    return inputs[0] * level;
  }

}