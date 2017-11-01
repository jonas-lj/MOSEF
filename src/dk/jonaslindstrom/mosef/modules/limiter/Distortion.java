package dk.jonaslindstrom.mosef.modules.limiter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Distortion extends SimpleModule {

	public Distortion(MOSEFSettings settings, Module input, Module distortion) {
		super(settings, input, distortion);
	}

	@Override
	public float getNextSample(float... inputs) {
		float x = inputs[0];
		float d = inputs[1];
		return (float) (x + Math.atan(16.0f * d * x) / 4.0f);
	}
	
}