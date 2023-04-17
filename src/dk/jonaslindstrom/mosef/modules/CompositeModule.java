package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import java.util.Map;

/**
 * A module composed of other modules. Implement the {@link #buildModule(MOSEFSettings, Map, Map)}
 * method to define how to construct the module.
 */
public abstract class CompositeModule implements Module {

  private final Module output;

  protected CompositeModule(MOSEFSettings settings) {
    this(settings, Map.of(), Map.of());
  }

  protected CompositeModule(MOSEFSettings settings, Map<String, Module> inputs) {
    this(settings, inputs, Map.of());
  }

  protected CompositeModule(MOSEFSettings settings, Map<String, Module> inputs,
      Map<String, Double> parameters) {
    this.output = buildModule(settings, inputs, parameters);
  }

  /**
   * Build the composite module and return the output module. The inputs and parameters maps
   * contains the inputs and parameters given in the contructor.
   */
  public abstract Module buildModule(MOSEFSettings settings, Map<String, Module> inputs,
      Map<String, Double> parameters);

  @Override
  public double[] getNextSamples() {
    return output.getNextSamples();
  }

}
