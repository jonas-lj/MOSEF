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
public abstract class SimpleModule implements MOSEFModule {

  protected MOSEFSettings settings;
  protected Map<String, MOSEFModule> inputs;
  private float[] buffer;
  private float[][] inputBuffers;

  private static Map<String, MOSEFModule> buildMap(String prefix, List<MOSEFModule> modules) {
    Map<String, MOSEFModule> map = new HashMap<>();
    for (int i = 0; i < modules.size(); i++) {
      String name = prefix + i;
      map.put(name, modules.get(i));
    }
    return map;
  }

  protected SimpleModule(MOSEFSettings settings, String prefix, List<MOSEFModule> modules) {
    this(settings, buildMap(prefix, modules));
  }

  protected SimpleModule(MOSEFSettings settings, Map<String, MOSEFModule> inputs) {
    this.settings = settings;
    this.inputs = inputs;

    this.buffer = new float[settings.getBufferSize()];
    this.inputBuffers = new float[inputs.size()][];
  }

  protected SimpleModule(MOSEFSettings settings, String name1, MOSEFModule input1, String name2,
      MOSEFModule input2, String name3, MOSEFModule input3) {
    this(settings, Map.of(name1, input1, name2, input2, name3, input3));
  }

  protected SimpleModule(MOSEFSettings settings, String name1, MOSEFModule input1, String name2,
      MOSEFModule input2) {
    this(settings, Map.of(name1, input1, name2, input2));
  }

  protected SimpleModule(MOSEFSettings settings, String name1, MOSEFModule input1) {
    this(settings, Map.of(name1, input1));
  }

  /**
   * Return the sample of the output given the the corresponding samples of the input signals. The
   * order of the input signals are the same as given in the constructor.
   * 
   * @param inputs
   * @return
   */
  public abstract float getNextSample(float... inputs);

  @Override
  public float[] getNextSamples() {

    int i = 0;
    for (MOSEFModule input : inputs.values()) {
      inputBuffers[i++] = input.getNextSamples();
    }

    for (int j = 0; j < settings.getBufferSize(); j++) {
      float[] current = new float[inputs.size()];
      for (int k = 0; k < inputs.size(); k++) {
        current[k] = inputBuffers[k][j];
      }
      buffer[j] = getNextSample(current);
    }

    return buffer;
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return inputs;
  }

}
