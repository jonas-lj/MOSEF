package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Clock extends SimpleModule {

  private int state;

  public Clock(MOSEFSettings settings, Module delta) {
    super(settings, delta);
    this.state = 0;
  }

  public void reset() {
    this.state = 0;
  }

  @Override
  public double getNextSample(double[] inputs) {
    state++;
    if (state >= settings.getSampleRate() * inputs[0]) {
      reset();
      return 1.0f;
    }
    return 0.0f;
  }

}
