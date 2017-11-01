package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class ClockFixed extends SimpleModule {

	private int state;
	private int delta;
	
	public ClockFixed(MOSEFSettings settings, int bpm) {
		super(settings);
		this.state = 0;
		this.delta = (int) (settings.getSampleRate() * 60.0 / bpm);
	}
	
	public void reset() {
		this.state = 0;
	}
	
	@Override
	public float getNextSample(float ... inputs) {
		state++;
		if (state == delta) {
			reset();
			return 1.0f;
		}
		return 0.0f;
	}

}
