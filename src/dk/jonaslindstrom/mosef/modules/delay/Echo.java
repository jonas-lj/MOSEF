package dk.jonaslindstrom.mosef.modules.delay;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Echo extends CompositeModule {

	private Module input;
	private int echos;
	private Module time;
	private float max;

	public Echo(MOSEF mosef, Module input, int echos, Module time, float max) {
		super(mosef);
		this.input = input;
		this.time = time;
		this.echos = echos;
		this.max = max;
	}
	
	@Override
	public Module buildModule(MOSEF m) {		
		Module[] split = m.split(input, echos + 1);
		Module[] times = m.split(time, echos);
		Module[] delays = new Module[echos];
		for (int i = 1; i <= echos; i++) {
			Module delay = m.amplifier(m.delay(split[i], m.amplifier(times[i], i), max * i), 0.2f);
			delays[i - 1] = delay;
		}
		return m.mixer(split[0], m.mixer(delays));
	}

}
