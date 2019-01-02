package dk.jonaslindstrom.mosef.modules.level;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Arrays;
import java.util.Map;

public class Level implements MOSEFModule {

	private MOSEFModule input;
	
	public Level(MOSEFModule input) {
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

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input);
  }
	
}
