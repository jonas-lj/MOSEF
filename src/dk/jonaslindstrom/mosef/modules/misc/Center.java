package dk.jonaslindstrom.mosef.modules.misc;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Center extends SimpleModule {

	/**
	 * This module performs an affine transformation on an input, so the output
	 * is <i>center + input * amplitude</i>.
	 * 
	 * @param settings
	 * @param input
	 * @param center
	 * @param amplitude
	 */
	public Center(MOSEFSettings settings, Module input, Module center, Module amplitude) {
		super(settings, input, center, amplitude);
	}

	@Override
	public float getNextSample(float... inputs) {
		float i = inputs[0];
		float c = inputs[1];
		float a = inputs[2];
		return c + a * i;
	}

}
