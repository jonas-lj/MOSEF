package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public class SquareWave extends SimpleWave {

	public SquareWave(MOSEFSettings settings) {
		super(settings);
	}

	@Override
	public float getSample(float t) {
		if (t < 0.5f) {
			return 1.0f;
		} else {
			return -1.0f;
		}
	}

}
