package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;

public class PulseWave implements Wave {

	private MOSEFModule pulseWidth;

	public PulseWave(MOSEFModule pulseWidth) {
		this.pulseWidth = pulseWidth;
	}
	
	@Override
	public float[] getSamples(float[] t) {	
		float[] widths= pulseWidth.getNextSamples();
		for (int i = 0; i < t.length; i++) {
			if (t[i] < widths[i]) {
				t[i] = 1.0f;
			} else {
				t[i] = -1.0f;
			}
		}
		return t;
	}

}
