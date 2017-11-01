package dk.jonaslindstrom.mosef.modules.mixer;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Mixer extends SimpleModule {

	/**
	 * Create a new mixer module with the given modules as input.
	 * 
	 * @param modules
	 */
	public Mixer(MOSEFSettings settings, Module... modules) {
		super(settings, modules);
	}

	@Override
	public float getNextSample(float... inputs) {
		
		float output = 0.0f;
		for (int i = 0; i < inputs.length; i++) {
			output += inputs[i];
		}
		return output;
	}



}
