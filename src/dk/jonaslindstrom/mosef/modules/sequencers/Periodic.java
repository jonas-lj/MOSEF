package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Periodic extends SimpleModule {

  private int state;
  private double[] frequencies;

  public Periodic(MOSEFSettings settings, Module a, double[] frequencies) {
    super(settings, "Trigger", a);
    this.state = 0;
    this.frequencies = frequencies;
  }

  @Override
  public double getNextSample(double... inputs) {
    if (inputs[0] > 0.0f) {
      state = (state + 1) % frequencies.length;
    }
    return frequencies[state];
  }

}
