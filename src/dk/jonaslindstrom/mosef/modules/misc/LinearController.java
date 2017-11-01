package dk.jonaslindstrom.mosef.modules.misc;

import java.util.Arrays;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

public class LinearController implements Module {

	private float min, max, control, value;
	private float[] buffer;

	public LinearController(MOSEFSettings settings, float min, float max) {
		this.min = min;
		this.max = max;
		this.buffer = new float[settings.getBufferSize()];
		setController(0.0f);
	}
	
	/**
	 * Control value is between 0.0  and 1.0.
	 * 
	 * @param value
	 */
	public void setController(float c) {
		this.control = c;
		this.value = min + (max - min) * control;
	}
	
	public float getControl() {
		return control;
	}

	@Override
	public float[] getNextSamples() {
		Arrays.fill(buffer, value);
		return buffer;
	}
}