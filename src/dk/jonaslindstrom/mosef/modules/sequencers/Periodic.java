package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Periodic extends SimpleModule {

  private int state;
  private float[] frequencies;

  public Periodic(MOSEFSettings settings, MOSEFModule a, float[] frequencies) {
    super(settings, "Trigger", a);
    this.state = 0;
    this.frequencies = frequencies;
  }

  @Override
  public float getNextSample(float... inputs) {
    if (inputs[0] > 0.0f) {
      state = (state + 1) % frequencies.length;
    }
    return frequencies[state];
  }

}
