package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Reverb extends CompositeModule {

	private Module input;

	public Reverb(MOSEF mosef, Module input) {
		super(mosef);
		this.input = input;
	}
	
	@Override
	public Module buildModule(MOSEF mosef) {
		int n = 50;
		Module[] delays = new Module[n];
		Module[] split = mosef.split(input, n + 1);
		for (int i = 1; i <= n; i++) {
			float time = (float) Math.random();
			delays[i - 1] = mosef.amplifier(mosef.delay(split[i], mosef.constant(time), time), 0.5f * (1.0f - time));
		}
		return mosef.mixer(split[0], mosef.mixer(delays));
	}

}
