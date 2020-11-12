package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Map;

public class Chorus extends CompositeModule {


  public Chorus(MOSEFSettings mosef, Module input, Module rate, Module wetness,
      Module depth) {
    super(mosef, Map.of("In", input, "Rate", rate, "Wetness", wetness, "Depth", depth));
  }

  @Override
  public Module buildModule(MOSEFSettings settings, Map<String, Module> inputs,
      Map<String, Double> parameters) {

    MOSEF m = new MOSEF(settings);

    Module[] split = m.split(inputs.get("In"), 2);

    // Create LFO centered around 0.005 with amplitude 0.004
    Module lfo = m.offset(m.sine(inputs.get("Rate")), 0.005f, inputs.get("Depth"));

    // One signal is delayed, another is not
    Module delay = m.amplifier(m.delay(split[0], lfo, 0.01f), inputs.get("Wetness"));

    // Mix signal and delayed signal
    Module chorus = m.mixer(delay, split[1]);

    return chorus;
  }

}
