package dk.jonaslindstrom.mosef.modules.limiter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * This class represents a limiter module where the input signal is limited by
 * some limit.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Limiter extends SimpleModule {


	public Limiter(MOSEFSettings settings, Module input, Module limit) {
		super(settings, input, limit);
	}
	
	@Override
	public float getNextSample(float ... inputs) {
		float input = inputs[0];
		float limit = inputs[1];
		
		if (input > limit) {
			return limit;
		} else if (input < -limit) {
			return -limit;
		} else {
			return input;
		}
	}

}
