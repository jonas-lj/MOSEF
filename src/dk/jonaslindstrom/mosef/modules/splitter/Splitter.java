package dk.jonaslindstrom.mosef.modules.splitter;

import dk.jonaslindstrom.mosef.modules.Module;

/**
 * This class splits a signal. However, only splitter module it self (the master) increments the
 * state of the input module, where the other output modules (the slaves) simply keep a copy of the
 * latest sample from the input module.
 */
public class Splitter {

  private final Module input;
  private final Module[] outputs;
  private double[] samples;

  private int master;

  private Splitter(Module input, int number) {
    this.input = input;

    outputs = new Module[number];

    this.master = 0;
    for (int i = 0; i < number; i++) {
      outputs[i] = new Split(i);
    }
  }

  public static Module[] split(Module input, int n) {
    Splitter splitter = new Splitter(input, n);
    return splitter.getOutputs();
  }

  private Module[] getOutputs() {
    return outputs;
  }

  private class Split implements Module {

    private final int i;

    public Split(int i) {
      this.i = i;
    }

    @Override
    public double[] getNextSamples() {
      // On the very first sample update, a non-master Split may be called before the others. In that case,
      // this Slave should be the master.
      if (samples == null && master != i) {
        master = i;
      }

      // Only the master calls the input module. Otherwise it would update multiple times per buffer update.
      if (i == master) {
        samples = input.getNextSamples();
      }

      return samples;
    }

  }
}
