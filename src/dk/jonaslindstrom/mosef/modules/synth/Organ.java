package dk.jonaslindstrom.mosef.modules.synth;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Organ extends CompositeModule {

	private static int[] overtoneRatios = new int[] { 2, 3, 4, 6, 8, 10, 12, 16 };
	private MOSEFModule frequency;
	private MOSEFModule[] drawbars;

	public Organ(MOSEF mosef, MOSEFModule frequency, MOSEFModule... drawbars) {
		super(mosef);
		this.frequency = frequency;
		this.drawbars = drawbars;
	}

	@Override
	public MOSEFModule buildModule(MOSEF m) {
		int numberOfOvertones = drawbars.length;

		MOSEFModule[] oscillators = new MOSEFModule[numberOfOvertones + 1];

		MOSEFModule[] splitFrequencies = m.split(frequency, numberOfOvertones + 1);
		oscillators[0] = m.triangle(splitFrequencies[0]);

		for (int i = 1; i <= numberOfOvertones; i++) {
			oscillators[i] = m.amplifier(m.triangle(m.multiplier(splitFrequencies[i], overtoneRatios[i - 1])), drawbars[i - 1]);
		}

		return m.amplifier(m.mixer(oscillators), m.constant(0.5f));	
	}

  @Override
  public Map<String, MOSEFModule> getInputs() {
    Map<String, MOSEFModule> out = Map.of("Frequency", frequency);
    for (int i = 0; i < drawbars.length; i++) {
      out.put("Drawbar" + i, drawbars[i]);
    }
    return out;
  }

}
