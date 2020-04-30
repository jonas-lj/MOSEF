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
		super(settings);
	}
	
	@Override
	public double getNextSample(double ... inputs) {
		return (double) (Math.random() * 2.0) - 1.0;
	}

}
