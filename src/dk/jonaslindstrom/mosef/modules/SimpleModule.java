package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.ArrayList;
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
  protected List<Module> inputs;
  private double[] buffer;
  private double[][] inputBuffers;

  protected SimpleModule(MOSEFSettings settings, List<Module> inputs) {
    this.settings = settings;
    this.inputs = inputs;
    this.buffer = new double[settings.getBufferSize()];
    this.inputBuffers = new double[inputs.size()][];
  }

  protected SimpleModule(MOSEFSettings settings, Module input1,
                         Module input2, Module input3, Module input4,
                         Module input5) {
    this(settings, List.of(input1, input2, input3, input4, input5));
  }

  protected SimpleModule(MOSEFSettings settings, Module input1,
                         Module input2, Module input3, Module input4) {
    this(settings, List.of(input1, input2, input3, input4));
  }

  protected SimpleModule(MOSEFSettings settings, Module input1,
                         Module input2, Module input3) {
    this(settings, List.of(input1, input2, input3));
  }

  protected SimpleModule(MOSEFSettings settings, Module input1,
                         Module input2) {
    this(settings, List.of(input1, input2));
  }

  protected SimpleModule(MOSEFSettings settings, Module input1) {
    this(settings, List.of(input1));
  }

  protected SimpleModule(MOSEFSettings settings) {
    this(settings, List.of());
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
    for (Module input : inputs) {
      inputBuffers[i++] = input.getNextSamples();
    }

    double[] current = new double[inputs.size()];
    for (int j = 0; j < settings.getBufferSize(); j++) {
      for (int k = 0; k < inputs.size(); k++) {
        current[k] = inputBuffers[k][j];
      }
      buffer[j] = getNextSample(current);
    }

    return buffer;
  }

}
