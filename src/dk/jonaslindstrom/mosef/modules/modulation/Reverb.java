package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Reverb extends CompositeModule {

	private MOSEFModule input;

	public Reverb(MOSEF mosef, MOSEFModule input) {
		super(mosef);
		this.input = input;
	}
	
	@Override
	public MOSEFModule buildModule(MOSEF mosef) {
		int n = 50;
		MOSEFModule[] delays = new MOSEFModule[n];
		MOSEFModule[] split = mosef.split(input, n + 1);
		for (int i = 1; i <= n; i++) {
			float time = (float) Math.random();
			delays[i - 1] = mosef.amplifier(mosef.delay(split[i], mosef.constant(time), time), 0.5f * (1.0f - time));
		}
		return mosef.mixer(split[0], mosef.mixer(delays));
	}

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input);
  }

}
