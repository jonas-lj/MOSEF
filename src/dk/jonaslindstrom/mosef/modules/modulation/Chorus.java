package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Chorus extends CompositeModule {

  private MOSEFModule input;
  private MOSEFModule rate;
  private MOSEFModule wetness;
  private MOSEFModule depth;

  public Chorus(MOSEF mosef, MOSEFModule input, MOSEFModule rate, MOSEFModule wetness,
      MOSEFModule depth) {
    super(mosef);
    this.input = input;
    this.rate = rate;
    this.wetness = wetness;
    this.depth = depth;
  }

  @Override
  public MOSEFModule buildModule(MOSEF m) {

    MOSEFModule[] split = m.split(input, 2);

    // Create LFO centered around 0.005 with amplitude 0.004
    MOSEFModule lfo = m.center(m.sine(rate), 0.005f, depth);

    // One signal is delayed, another is not
    MOSEFModule delay = m.amplifier(m.delay(split[0], lfo, 0.01f), wetness);

    // Mix signal and delayed signal
    MOSEFModule chorus = m.mixer(delay, split[1]);

    return chorus;
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input, "Rate", rate, "Mix", wetness, "Depth", depth);
  }

}
