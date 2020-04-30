package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Rhythm extends SimpleModule {

  private int[] rhythm;
  private int b;

  public Rhythm(MOSEFSettings settings, Module clock, int[] rhythm) {
    super(settings, clock);
    this.rhythm = rhythm;
    this.b = 0;
  }

  @Override
  public double getNextSample(double... inputs) {
    double out = 0.0f;
    if (inputs[0] > 0.0f) {
      if (rhythm[b] != 0) {
        out = 1.0f;
      }
      b = (b + 1) % rhythm.length;
    }
    return out;
  }

}
