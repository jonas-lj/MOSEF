package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Chorus extends CompositeModule {

	private Module input;
	private Module rate;
	private Module wetness;
	private Module depth;

	public Chorus(MOSEF mosef, Module input, Module rate, Module wetness, Module depth) {
		super(mosef);
		this.input = input;
		this.rate = rate;
		this.wetness = wetness;
		this.depth = depth;
	}
	
	@Override
	public Module buildModule(MOSEF m) {
		
		Module[] split = m.split(input, 2);

		// Create LFO centered around 0.005 with amplitude 0.004
		Module lfo = m.center(m.sine(rate), 0.005f, depth);

		// One signal is delayed, another is not
		Module delay = m.amplifier(m.delay(split[0], lfo, 0.01f), wetness);

		// Mix signal and delayed signal
		Module chorus = m.mixer(delay, split[1]);

		return chorus;
	}

}
