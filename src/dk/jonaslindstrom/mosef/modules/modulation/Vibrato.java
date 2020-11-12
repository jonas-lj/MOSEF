package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Map;

public class Vibrato extends CompositeModule {

  public Vibrato(MOSEFSettings mosef, Module input, Module rate, Module depth) {
    super(mosef, Map.of("Input", input, "Rate", rate, "Depth", depth));
  }

  @Override
  public Module buildModule(MOSEFSettings settings, Map<String, Module> inputs,
      Map<String, Double> parameters) {
    MOSEF mosef = new MOSEF(settings);
    Module sine = mosef.offset(mosef.sine(inputs.get("Rate")), 0.5f, inputs.get("Depth"));
    return mosef.amplifier(sine, inputs.get("Input"));
  }


}
