package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class ClockDivider extends SimpleModule {

  private int divisor;
  private int counter;

  public ClockDivider(MOSEFSettings settings, MOSEFModule clock, int divisor) {
    super(settings, "Clock", clock);
    this.divisor = divisor;
    this.counter = 0;

  }

  @Override
  public float getNextSample(float... inputs) {
    if (inputs[0] == 1.0f) {
      counter++;
      if (counter == divisor) {
        counter = 0;
        return 1.0f;
      }
    }
    return 0.0f;
  }


}
