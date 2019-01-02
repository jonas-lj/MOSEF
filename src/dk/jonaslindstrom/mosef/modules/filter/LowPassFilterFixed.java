package dk.jonaslindstrom.mosef.modules.filter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.filter.filters.LowPassDiscreteFilterFactory;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.HammingWindow;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.Window;
import java.util.Map;

public class LowPassFilterFixed implements MOSEFModule {

	private float[] discreteFilter;
	private SampleMemory sampleMemory;
	private MOSEFModule input;
	private int length;
	private float[] buffer;

	public LowPassFilterFixed(MOSEFSettings settings, MOSEFModule input, float cutoff, int length,
			Window window) {
		this.buffer = new float[settings.getBufferSize()];
		this.input = input;
		this.length = length;

		this.discreteFilter = new LowPassDiscreteFilterFactory((int) cutoff, length,
				settings.getSampleRate(), window).getSamples();
		this.sampleMemory = new SampleMemory(length);
	}

	public LowPassFilterFixed(MOSEFSettings settings, MOSEFModule input, float cutoff) {
		this(settings, input, cutoff, 101, new HammingWindow(101));
	}

	
	@Override
	public float[] getNextSamples() {
		float[] inputs = input.getNextSamples();
		for (int i = 0; i < inputs.length; i++) {
			this.sampleMemory.push(inputs[i]);
			float c = 0.0f;
			for (int j = 0; j < this.length; j++) {
				c += sampleMemory.get(j) * discreteFilter[j];
			}
			buffer[i] = c; // Reuse the buffer of the input module
		}
		return buffer;
	}

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input);
  }

}
