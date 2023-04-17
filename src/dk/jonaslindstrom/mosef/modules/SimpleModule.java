package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import java.util.List;

/**
 * This is an implementation of a module where the ouput at a specific sample only depends on the
 * corresponding sample of the input signals.
 */
public abstract class SimpleModule implements Module {

  protected final MOSEFSettings settings;
  protected final List<Module> inputs;
  private final double[] buffer;
  private final double[][] inputBuffers;
  private final double[] current;

  protected SimpleModule(MOSEFSettings settings, List<Module> inputs) {
    this.settings = settings;
    this.inputs = inputs;
    this.buffer = new double[settings.getBufferSize()];
    this.inputBuffers = new double[inputs.size()][];
    this.current = new double[inputs.size()];
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
   */
  public abstract double getNextSample(double[] inputs);

  @Override
  public double[] getNextSamples() {

    for (int i = 0; i < inputs.size(); i++) {
      inputBuffers[i] = inputs.get(i).getNextSamples();
    }

    for (int j = 0; j < settings.getBufferSize(); j++) {
      for (int i = 0; i < inputs.size(); i++) {
        current[i] = inputBuffers[i][j];
      }
      buffer[j] = getNextSample(current);
    }

    return buffer;
  }

}