package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Arrays;
import java.util.Map;

public class ExponentialController implements MOSEFModule {

  private float min, max, control;
  private float[] buffer;

  public ExponentialController(MOSEFSettings settings, float min, float max) {
    this.min = min;
    this.max = max;
    this.buffer = new float[settings.getBufferSize()];
    setController(0.0f);
  }

  /**
   * Control value is between 0.0 and 1.0.
   * 
   * @param value
   */
  public void setController(float c) {
    this.control = c;
    float value = (float) (min + (max - min) * Math.expm1(control) / Math.E);
    Arrays.fill(buffer, value);
  }

  public float getControl() {
    return control;
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
