package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Vibrato extends CompositeModule {

	private Module input;
	private Module rate;
	private Module depth;

	public Vibrato(MOSEF mosef, Module input, Module rate, Module depth) {
		super(mosef);
		this.input = input;
		this.rate = rate;
		this.depth = depth;
	}
	
	@Override
	public Module buildModule(MOSEF mosef) {
		Module sine = mosef.center(mosef.sine(rate), 0.5f, depth);
		return mosef.amplifier(sine, input);
	}

}
