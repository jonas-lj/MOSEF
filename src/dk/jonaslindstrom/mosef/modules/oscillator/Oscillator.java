package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.Wave;

public class Oscillator implements Module {

	private Wave wave;
	private Module frequency;
	private float t = 0.0f;
	private float scale;
	private float[] buffer;
	
	/**
	 * Create a new oscillator.
	 * 
	 * @param frequency A module controlling the frequency of the oscillator.
	 * @param wave A {@link Wave} function.
	 * @param samplerate The number of samples per second.
	 */
	public Oscillator(MOSEFSettings settings, Module frequency, Wave wave) {
		this.buffer = new float[settings.getBufferSize()];
		this.wave = wave;
		this.frequency = frequency;
		
		this.scale = 1.0f / settings.getSampleRate();
	}
	
	@Override
	public float[] getNextSamples() {
		
		float[] frequencies = frequency.getNextSamples();
		
		for (int i = 0; i < frequencies.length; i++) {
			t += frequencies[i] * scale;
			while (t > 1.0f) {
				t -= 1.0f;
			}
			buffer[i] = t;
		}
		return wave.getSamples(buffer);
		
	}
	
}
