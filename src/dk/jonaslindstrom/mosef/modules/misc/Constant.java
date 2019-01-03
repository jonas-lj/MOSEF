package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;
import java.util.Map;

public class Constant implements Module {

  private double[] buffer;

  /**
   * Create a new constant module with the given value. Every time a sample is requested the
   * constant is returned.
   * 
   * @param value
   */
  public Constant(MOSEFSettings settings, double value) {
    this.buffer = new double[settings.getBufferSize()];
    Arrays.fill(buffer, value);
  }

  @Override
  public double[] getNextSamples() {
    return buffer;
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of();
  }

}
