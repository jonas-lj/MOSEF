package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;
import java.util.Map;

public class LinearController implements Module {

  private double min, max, control, value;
  private double[] buffer;

  public LinearController(MOSEFSettings settings, double min, double max) {
    this.min = min;
    this.max = max;
    this.buffer = new double[settings.getBufferSize()];
    setController(0.0f);
  }

  /**
   * Control value is between 0.0 and 1.0.
   * 
   * @param value
   */
  public void setController(double c) {
    this.control = c;
    this.value = min + (max - min) * control;
  }

  public double getControl() {
    return control;
  }

  @Override
  public double[] getNextSamples() {
    Arrays.fill(buffer, value);
    return buffer;
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of();
  }
}
