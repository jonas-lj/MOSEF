package dk.jonaslindstrom.mosef.modules.splitter;

import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Arrays;
import java.util.Map;

/**
 * This class splits a signal. However, only splitter module it self (the master) increments the
 * state of the input module, where the other output modules (the slaves) simply keep a copy of the
 * latest sample from the input module.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Splitter {

  private Module input;
  private Module[] outputs;
  private double[][] samples;

  public static Module[] split(Module input, int n) {
    Splitter splitter = new Splitter(input, n);
    return splitter.getOutputs();
  }

  private Splitter(Module input, int number) {
    this.input = input;

    this.outputs = new Module[number];
    outputs[0] = new Master();
    for (int i = 1; i < number; i++) {
      outputs[i] = new Slave(i);
    }
    this.samples = new double[number][];
  }

  private Module[] getOutputs() {
    return outputs;
  }

  private class Master implements Module {

    @Override
    public double[] getNextSamples() {
      samples[0] = input.getNextSamples();
      for (int i = 1; i < samples.length; i++) {
        samples[i] = Arrays.copyOf(samples[0], samples[0].length);
      }
      return samples[0];
    }

    @Override
    public Map<String, Module> getInputs() {
      return Map.of("In", input);
    }

  }

  private class Slave implements Module {

    private int i;

    public Slave(int i) {
      this.i = i;
    }

    @Override
    public double[] getNextSamples() {
      return samples[i];
    }

    @Override
    public Map<String, Module> getInputs() {
      return Map.of("In", input);
    }

  }
}
