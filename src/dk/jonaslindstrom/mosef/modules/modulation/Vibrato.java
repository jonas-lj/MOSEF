package dk.jonaslindstrom.mosef.modules.modulation;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Vibrato extends CompositeModule {

	private MOSEFModule input;
	private MOSEFModule rate;
	private MOSEFModule depth;

	public Vibrato(MOSEF mosef, MOSEFModule input, MOSEFModule rate, MOSEFModule depth) {
		super(mosef);
		this.input = input;
		this.rate = rate;
		this.depth = depth;
	}
	
	@Override
	public MOSEFModule buildModule(MOSEF mosef) {
		MOSEFModule sine = mosef.center(mosef.sine(rate), 0.5f, depth);
		return mosef.amplifier(sine, input);
	}

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input, "Rate", rate, "Depth", depth);
  }

}
