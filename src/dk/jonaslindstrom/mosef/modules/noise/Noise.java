package dk.jonaslindstrom.mosef.modules.noise;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.Map;

/**
 * This module can create white noise using the {@link Math#random} method.
 * 
 * @author jonas
 *
 */
public class Noise extends SimpleModule {

	public Noise(MOSEFSettings settings) {
		super(settings, Map.of());
	}
	
	@Override
	public float getNextSample(float ... inputs) {
		return (float) (Math.random() * 2.0f) - 1.0f;
	}

}
