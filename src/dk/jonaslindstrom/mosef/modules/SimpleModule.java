package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of a module where the ouput at a specific sample only depends on the
 * corresponding sample of the input signals.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public abstract class SimpleModule implements Module {

  protected MOSEFSettings settings;
  protected Map<String, Module> inputs;
  private double[] buffer;
  private double[][] inputBuffers;

  private static Map<String, Module> buildMap(String prefix, List<Module> modules) {
    Map<String, Module> map = new HashMap<>();
    for (int i = 0; i < modules.size(); i++) {
      String name = prefix + i;
      map.put(name, modules.get(i));
    }
    return map;
  }

  protected SimpleModule(MOSEFSettings settings, String prefix, List<Module> modules) {
    this(settings, buildMap(prefix, modules));
  }

  protected SimpleModule(MOSEFSettings settings, Map<String, Module> inputs) {
    this.settings = settings;
    this.inputs = inputs;

    this.buffer = new double[settings.getBufferSize()];
    this.inputBuffers = new double[inputs.size()][];
  }

  protected SimpleModule(MOSEFSettings settings, String name1, Module input1, String name2,
      Module input2, String name3, Module input3) {
    this(settings, Map.of(name1, input1, name2, input2, name3, input3));
  }

  protected SimpleModule(MOSEFSettings settings, String name1, Module input1, String name2,
      Module input2) {
    this(settings, Map.of(name1, input1, name2, input2));
  }

  protected SimpleModule(MOSEFSettings settings, String name1, Module input1) {
    this(settings, Map.of(name1, input1));
  }

  /**
   * Return the sample of the output given the the corresponding samples of the input signals. The
   * order of the input signals are the same as given in the constructor.
   * 
   * @param inputs
   * @return
   */
  public abstract double getNextSample(double... inputs);

  @Override
  public double[] getNextSamples() {

    int i = 0;
    for (Module input : inputs.values()) {
      inputBuffers[i++] = input.getNextSamples();
    }

    for (int j = 0; j < settings.getBufferSize(); j++) {
      double[] current = new double[inputs.size()];
      for (int k = 0; k < inputs.size(); k++) {
        current[k] = inputBuffers[k][j];
      }
      buffer[j] = getNextSample(current);
    }

    return buffer;
  }

  @Override
  public Map<String, Module> getInputs() {
    return inputs;
  }

}
