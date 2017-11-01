package dk.jonaslindstrom.mosef.modules.misc;

import java.util.Arrays;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

public class Constant implements Module {

	private float[] buffer;

	/**
	 * Create a new constant module with the given value. Every time a sample is
	 * requested the constant is returned.
	 * 
	 * @param value
	 */
	public Constant(MOSEFSettings settings, float value) {
		this.buffer = new float[settings.getBufferSize()];
		Arrays.fill(buffer, value);
	}
	
	@Override
	public float[] getNextSamples() {
		return buffer;
	}

}
