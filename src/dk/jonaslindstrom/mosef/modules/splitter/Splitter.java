package dk.jonaslindstrom.mosef.modules.splitter;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;
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

  private MOSEFModule input;
  private MOSEFModule[] outputs;
  private float[][] samples;

  public static MOSEFModule[] split(MOSEFModule input, int n) {
    Splitter splitter = new Splitter(input, n);
    return splitter.getOutputs();
  }

  private Splitter(MOSEFModule input, int number) {
    this.input = input;

    this.outputs = new MOSEFModule[number];
    outputs[0] = new Master();
    for (int i = 1; i < number; i++) {
      outputs[i] = new Slave(i);
    }
    this.samples = new float[number][];
  }

  private MOSEFModule[] getOutputs() {
    return outputs;
  }

  private class Master implements MOSEFModule {

    @Override
    public float[] getNextSamples() {
      samples[0] = input.getNextSamples();
      for (int i = 1; i < samples.length; i++) {
        samples[i] = Arrays.copyOf(samples[0], samples[0].length);
      }
      return samples[0];
    }

    @Override
    public Map<String, MOSEFModule> getInputs() {
      return Map.of("In", input);
    }

  }

  private class Slave implements MOSEFModule {

    private int i;

    public Slave(int i) {
      this.i = i;
    }

    @Override
    public float[] getNextSamples() {
      return samples[i];
    }

    @Override
    public Map<String, MOSEFModule> getInputs() {
      return Map.of("In", input);
    }

  }
}
