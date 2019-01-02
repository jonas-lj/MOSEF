package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Clock extends SimpleModule {

  private int state;

  public Clock(MOSEFSettings settings, MOSEFModule delta) {
    super(settings, "Delta", delta);
    this.state = 0;
  }

  public void reset() {
    this.state = 0;
  }

  @Override
  public float getNextSample(float... inputs) {
    state++;
    if (state == settings.getSampleRate() * inputs[0]) {
      reset();
      return 1.0f;
    }
    return 0.0f;
  }

}
