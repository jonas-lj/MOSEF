package dk.jonaslindstrom.mosef.modules.filter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.filter.filters.LowPassDiscreteFilterFactory;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.RectangularWindow;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.Window;
import java.util.Map;

public class LowPassFilter implements Module {

  private Module cutoff;
  private double[][] discreteFilters;
  private Module input;
  private SampleMemory sampleMemory;
  private int length;
  private double scale;
  private double[] buffer;

  public LowPassFilter(MOSEFSettings settings, Module input, Module cutoff) {
    // Default values
    this(settings, input, cutoff, 512, 101, new RectangularWindow(101));
  }

  /**
   * @param settings Global settings
   * 
   * @param input Input module
   * @param cutoff Module controlling the cutoff frequency
   * @param size The number of filters to be generated. More allows for smoother change in cutoff
   *        frequency, but requires more memory.
   * @param length The length of the filters to be used.
   * @param window The window function to be used in the filter.
   */
  public LowPassFilter(MOSEFSettings settings, Module input, Module cutoff, int size,
      int length, Window window) {
    this.buffer = new double[settings.getBufferSize()];
    this.input = input;
    this.length = length;
    this.cutoff = cutoff;
    this.scale = 2.0f * size / settings.getSampleRate();

    /*
     * We only need to consider frequencies up to half the samplerate, e.g. the Shannon-Nyquist
     * frequency.
     */
    double d = settings.getSampleRate() * 0.5f / size;
    this.discreteFilters = new double[size][length];
    for (int i = 0; i < size; i++) {
      discreteFilters[i] =
          new LowPassDiscreteFilterFactory((int) (i * d), length, settings.getSampleRate(), window)
              .getSamples();
    }

    this.sampleMemory = new SampleMemory(length);
  }

  @Override
  public double[] getNextSamples() {
    double[] cutoffs = cutoff.getNextSamples();
    double[] inputs = input.getNextSamples();
    for (int i = 0; i < cutoffs.length; i++) {
      sampleMemory.push(inputs[i]);

      int fcIndex = (int) (cutoffs[i] * scale);

      double c = 0.0f;
      for (int j = 0; j < this.length; j++) {
        c += sampleMemory.get(j) * discreteFilters[fcIndex][j];
      }
      buffer[i] = c; // Reuse the buffer of the input module
    }
    return buffer;
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of("In", input, "Cutoff", cutoff);
  }

}
