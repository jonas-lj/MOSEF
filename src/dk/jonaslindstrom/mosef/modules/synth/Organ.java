package dk.jonaslindstrom.mosef.modules.synth;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Map;

public class Organ extends CompositeModule {

  private static int[] overtoneRatios = new int[] {2, 3, 4, 6, 8, 10, 12, 16};

  public Organ(MOSEFSettings settings, Module frequency, Module... drawbars) {
    super(settings, buildInputsMap(frequency, drawbars));
  }

  @Override
  public Module buildModule(MOSEFSettings settings, Map<String, Module> inputs, Map<String, Double> parameters) {

    MOSEF m = new MOSEF(settings);

    int numberOfOvertones = inputs.size() - 1;

    Module[] oscillators = new Module[numberOfOvertones + 1];

    Module[] splitFrequencies = m.split(inputs.get("Frequency"), numberOfOvertones + 1);
    oscillators[0] = m.triangle(splitFrequencies[0]);

    for (int i = 1; i <= numberOfOvertones; i++) {
      oscillators[i] = m.amplifier(
          m.triangle(m.multiplier(splitFrequencies[i], overtoneRatios[i - 1])), inputs.get("Drawbar" + (i - 1)));
    }

    return m.amplifier(m.mixer(oscillators), m.constant(0.5f));
  }

  public static Map<String, Module> buildInputsMap(Module frequency, Module... drawbars) {
    Map<String, Module> out = Map.of("Frequency", frequency);
    for (int i = 0; i < drawbars.length; i++) {
      out.put("Drawbar" + i, drawbars[i]);
    }
    return out;
  }

}
