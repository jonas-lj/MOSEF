package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.Map;

public class ClockFixed extends SimpleModule {

  private int state;
  private final int delta;

  public ClockFixed(MOSEFSettings settings, int bpm) {
    super(settings);
    this.state = 0;
    this.delta = settings.getSampleRate() * 60 / bpm;
  }

  @Override
  public double getNextSample(double... inputs) {
    if (++state == delta) {
      this.state = 0;
      return 1.0;
    }
    return 0.0;
  }

}
