package dk.jonaslindstrom.mosef.modules.polyphony;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;

public class DynamicModule implements Module {

  private final double[] buffer;

  public DynamicModule(MOSEFSettings settings) {
    this.buffer = new double[settings.getBufferSize()];
  }

  public synchronized void setOutput(double value) {
    Arrays.fill(buffer, value);
  }

  @Override
  public double[] getNextSamples() {
    return buffer;
  }
}
