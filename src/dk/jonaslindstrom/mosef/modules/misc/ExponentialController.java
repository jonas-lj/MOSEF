package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;
import java.util.Map;

public class ExponentialController implements Module {

  private double min, max, control;
  private double[] buffer;

  public ExponentialController(MOSEFSettings settings, double min, double max) {
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
    double value = (double) (min + (max - min) * Math.expm1(control) / Math.E);
    Arrays.fill(buffer, value);
  }

  public double getControl() {
    return control;
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
