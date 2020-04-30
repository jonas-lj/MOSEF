package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class ClockDivider extends SimpleModule {

  private int divisor;
  private int counter;

  public ClockDivider(MOSEFSettings settings, Module clock, int divisor) {
    super(settings, clock);
    this.divisor = divisor;
    this.counter = 0;

  }

  @Override
  public double getNextSample(double... inputs) {
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
