package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Ensemble extends CompositeModule {

  private MOSEFModule input;

  public Ensemble(MOSEF mosef, MOSEFModule input) {
    super(mosef);
    this.input = input;
  }

  @Override
  public MOSEFModule buildModule(MOSEF m) {
    MOSEFModule[] split = m.split(input, 3);
    MOSEFModule ch1 = m.chorus(split[0], m.constant(0.22f), m.constant(0.7f));
    MOSEFModule ch2 = m.chorus(split[1], m.constant(0.13f), m.constant(0.7f));
    MOSEFModule ch3 = m.chorus(split[2], m.constant(0.38f), m.constant(0.7f));
    return m.mixer(ch1, ch2, ch3);
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input);
  }

}
