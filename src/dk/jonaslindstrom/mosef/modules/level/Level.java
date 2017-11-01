package dk.jonaslindstrom.mosef.modules.level;

import java.util.Arrays;

import dk.jonaslindstrom.mosef.modules.Module;

public class Level implements Module {

	private Module input;
	
	public Level(Module input) {
		this.input = input;
	}

	@Override
	public float[] getNextSamples() {
		float[] buffer = input.getNextSamples();

		float mean = 0.0f;
		for (float s : buffer) {
			mean += s;
		}
		mean /= buffer.length;
		
		Arrays.fill(buffer, mean);
		return buffer;
	}
	
}
