package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Exponential extends SimpleModule {

	/**
	 * This modules maps a linear signal to an exponential one s.t. 0.0 maps to 0.0 and
	 * 1.0 maps to 1.0.
	 * 
	 * @param settings
	 * @param input
	 */
	public Exponential(MOSEFSettings settings, Module input) {
		super(settings, input);
	}

	@Override
	public float getNextSample(float... inputs) {
		return (float) (Math.expm1(inputs[0]) / Math.E);
	}

	
	
}
