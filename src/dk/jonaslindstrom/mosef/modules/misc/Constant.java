package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Arrays;
import java.util.Map;

public class Constant implements MOSEFModule {

  private float[] buffer;

  /**
   * Create a new constant module with the given value. Every time a sample is requested the
   * constant is returned.
   * 
   * @param value
   */
  public Constant(MOSEFSettings settings, float value) {
    this.buffer = new float[settings.getBufferSize()];
    Arrays.fill(buffer, value);
  }

  @Override
  public float[] getNextSamples() {
    return buffer;
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of();
  }

}
