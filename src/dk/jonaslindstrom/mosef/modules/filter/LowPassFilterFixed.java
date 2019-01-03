package dk.jonaslindstrom.mosef.modules.filter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.filter.filters.LowPassDiscreteFilterFactory;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.HammingWindow;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.Window;
import java.util.Map;

public class LowPassFilterFixed implements Module {

  private double[] discreteFilter;
  private SampleMemory sampleMemory;
  private Module input;
  private int length;
  private double[] buffer;

  public LowPassFilterFixed(MOSEFSettings settings, Module input, double cutoff, int length,
      Window window) {
    this.buffer = new double[settings.getBufferSize()];
    this.input = input;
    this.length = length;

    this.discreteFilter =
        new LowPassDiscreteFilterFactory((int) cutoff, length, settings.getSampleRate(), window)
            .getSamples();
    this.sampleMemory = new SampleMemory(length);
  }

  public LowPassFilterFixed(MOSEFSettings settings, Module input, double cutoff) {
    this(settings, input, cutoff, 101, new HammingWindow(101));
  }


  @Override
  public double[] getNextSamples() {
    double[] inputs = input.getNextSamples();
    for (int i = 0; i < inputs.length; i++) {
      this.sampleMemory.push(inputs[i]);
      double c = 0.0f;
      for (int j = 0; j < this.length; j++) {
        c += sampleMemory.get(j) * discreteFilter[j];
      }
      buffer[i] = c; // Reuse the buffer of the input module
    }
    return buffer;
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of("In", input);
  }

}
