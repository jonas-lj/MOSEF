package dk.jonaslindstrom.mosef.modules.synth;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Organ extends CompositeModule {

	private static int[] overtoneRatios = new int[] { 2, 3, 4, 6, 8, 10, 12, 16 };
	private Module frequency;
	private Module[] drawbars;

	public Organ(MOSEF mosef, Module frequency, Module... drawbars) {
		super(mosef);
		this.frequency = frequency;
		this.drawbars = drawbars;
	}

	@Override
	public Module buildModule(MOSEF m) {
		int numberOfOvertones = drawbars.length;

		Module[] oscillators = new Module[numberOfOvertones + 1];

		Module[] splitFrequencies = m.split(frequency, numberOfOvertones + 1);
		oscillators[0] = m.triangle(splitFrequencies[0]);

		for (int i = 1; i <= numberOfOvertones; i++) {
			oscillators[i] = m.amplifier(m.triangle(m.multiplier(splitFrequencies[i], overtoneRatios[i - 1])), drawbars[i - 1]);
		}

		return m.amplifier(m.mixer(oscillators), m.constant(0.5f));	
	}

}
