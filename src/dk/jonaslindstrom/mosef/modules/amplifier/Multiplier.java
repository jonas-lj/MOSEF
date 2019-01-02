package dk.jonaslindstrom.mosef.modules.amplifier;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * Instances of this class represents amplifier modules, where a signal form an
 * input module is multiplied with the value of another module.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Multiplier extends SimpleModule {

	/**
	 * Create a new amplifier module taking an input module and a level module.
	 * The output will be the product of the two modules.
	 * 
	 * @param input
	 * @param level
	 */
	public Multiplier(MOSEFSettings settings, MOSEFModule input, MOSEFModule level) {
		super(settings, "In", input, "Level", level);
	}

	@Override
	public float getNextSample(float... inputs) {
		return inputs[0] * inputs[1];
	}

}
