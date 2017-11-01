package dk.jonaslindstrom.mosef.modules.amplifier;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * Instances of this class represents amplifier modules, where a signal form an
 * input module is multiplied with the value of another module.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Amplifier extends SimpleModule {

	/**
	 * Create a new amplifier module taking an input module and a level module.
	 * The output will be the product of the two modules.
	 * 
	 * @param input
	 * @param level
	 */
	public Amplifier(MOSEFSettings settings, Module input, Module level) {
		super(settings, input, level);
	}

	@Override
	public float getNextSample(float... inputs) {
		float level = Math.max(0.0f, inputs[1]);
		return inputs[0] * level;
	}



}
