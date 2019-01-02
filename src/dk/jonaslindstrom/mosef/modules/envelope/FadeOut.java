package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class FadeOut extends SimpleModule {

	private enum FaderStatus {
		ON, OFF
	}

	private float dt;
	private FaderStatus status;
	private float t;

	public FadeOut(MOSEFSettings settings, MOSEFModule input, MOSEFModule length, MOSEFModule gate) {
		super(settings, "Input", input, "Length", length, "Gate", gate);
		this.dt = 1.0f / settings.getSampleRate();
		this.status = FaderStatus.OFF;
	}

	@Override
	public float getNextSample(float... inputs) {

		float i = inputs[0];
		float l = inputs[1];
		float g = inputs[2];

		switch (this.status) {
			case OFF:
			default:
				if (g > 0.0f) {
					this.t = 0.0f;
					this.status = FaderStatus.ON;
				}
				return i;
				
			case ON:
				t += dt;				
				if (t > l) {
					this.status = FaderStatus.OFF;					
				}
				return (t / l) * i;
		}
	}

}
