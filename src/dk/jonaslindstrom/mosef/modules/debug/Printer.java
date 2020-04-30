package dk.jonaslindstrom.mosef.modules.debug;

import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.List;
import java.util.Map;

public class Printer implements Module {

  private Module input;
  private int frequency;
  private int counter;

  /**
   * Instances of this module replicates the input module and prints the value of each
   * <i>frequency</i>th value to stdout.
   * 
   * @param input
   * @param frequency
   */
  public Printer(Module input, int frequency) {
    this.input = input;
    this.frequency = frequency;
    this.counter = 0;
  }

  @Override
  public double[] getNextSamples() {
    double[] buffer = input.getNextSamples();

    for (int i = 0; i < buffer.length; i++) {
      counter++;
      if (counter == frequency) {
        System.out.println(buffer[i]);
        counter = 0;
      }
    }
    return buffer;
  }

}
