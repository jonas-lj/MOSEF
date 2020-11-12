package dk.jonaslindstrom.mosef.modules.level;

import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;

public class Level implements Module {

  private final Module input;

  public Level(Module input) {
    this.input = input;
  }

  @Override
  public double[] getNextSamples() {
    double[] buffer = input.getNextSamples();

    double mean = 0.0f;
    for (double s : buffer) {
      mean += s;
    }
    mean /= buffer.length;

    Arrays.fill(buffer, mean);
    return buffer;
  }

}
