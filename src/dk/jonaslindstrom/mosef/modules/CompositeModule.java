package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CompositeModule implements Module {

  private final List<Pair<String, Module>> inputs;
  private Module output;

  protected CompositeModule(MOSEFSettings settings, Map<String, Module> inputs) {
    this(settings, inputs, Map.of());
  }

  protected CompositeModule(MOSEFSettings settings, Map<String, Module> inputs, Map<String, Double> parameters) {
    this.output = buildModule(settings, inputs, parameters);
    this.inputs = inputs.keySet().stream().map(k -> Pair.of(k, inputs.get(k))).collect(Collectors.toList());
  }

  /**
   * Build the composite module and return the output module.
   * 
   * @param settings
   * @return
   */
  public abstract Module buildModule(MOSEFSettings settings, Map<String, Module> inputs, Map<String, Double> parameters);

  @Override
  public double[] getNextSamples() {
    return output.getNextSamples();
  }

}
