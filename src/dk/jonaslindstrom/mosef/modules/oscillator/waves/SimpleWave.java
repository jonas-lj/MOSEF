package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public abstract class SimpleWave implements Wave {

	private float[] buffer;

	public SimpleWave(MOSEFSettings settings) {
		this.buffer = new float[settings.getBufferSize()];
	}
	
	public abstract float getSample(float t);
	
	@Override
	public float[] getSamples(float[] t) {
		for (int i = 0; i < t.length; i++) {
			buffer[i] = getSample(t[i]);
		}
		return buffer;
	}

	
}
