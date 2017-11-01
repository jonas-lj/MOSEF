package dk.jonaslindstrom.mosef.modules.noise;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * This module can create white noise using the {@link Math#random} method.
 * 
 * @author jonas
 *
 */
public class Noise extends SimpleModule {

	public Noise(MOSEFSettings settings) {
		super(settings);
	}
	
	@Override
	public float getNextSample(float ... inputs) {
		return (float) (Math.random() * 2.0f) - 1.0f;
	}

}
