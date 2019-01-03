package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Map;

public class Ensemble extends CompositeModule {

  private Module input;

  public Ensemble(MOSEF mosef, Module input) {
    super(mosef);
    this.input = input;
  }

  @Override
  public Module buildModule(MOSEF m) {
    Module[] split = m.split(input, 3);
    Module ch1 = m.chorus(split[0], m.constant(0.22f), m.constant(0.7f));
    Module ch2 = m.chorus(split[1], m.constant(0.13f), m.constant(0.7f));
    Module ch3 = m.chorus(split[2], m.constant(0.38f), m.constant(0.7f));
    return m.mixer(ch1, ch2, ch3);
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of("In", input);
  }

}
