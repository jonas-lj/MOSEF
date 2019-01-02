package dk.jonaslindstrom.mosef.modules.tuning;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.tuning.interpolate.InterpolateFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;

public class GlidingTuner extends SimpleModule implements Tuner {

	private enum GliderStatus {
		ON, OFF
	};

	private TuningFunction tuningFunction;
	private InterpolateFunction interpolateFunction;
	private float frequency;
	private float t, start, goal;
	private GliderStatus status;
	private int note;

	public GlidingTuner(MOSEFSettings settings, MOSEFModule speed, TuningFunction tuningFunction,
			InterpolateFunction interpolateFunction) {
		super(settings, "Speed", speed);
		this.tuningFunction = tuningFunction;
		this.interpolateFunction = interpolateFunction;
		this.frequency = tuningFunction.getFrequency(0);
	}

	@Override
	public void setNote(int note) {
		this.note = note;
		this.start = this.frequency;
		this.goal = tuningFunction.getFrequency(note);
		this.status = GliderStatus.ON;
		this.t = 0.0f;
	}

	@Override
	public int getNote() {
		return note;
	}

	@Override
	public float getNextSample(float... inputs) {

		// TODO: Use a trigger instead
		
		float dt = inputs[0];
		switch (status) {
			case ON:
				this.t += dt;
				this.frequency = start + (goal - start) * interpolateFunction.interpolate(t);
				if (t >= 1.0) {
					this.status = GliderStatus.OFF;
					this.frequency = this.goal;
				}
				return this.frequency;

			case OFF:
			default:
				return this.frequency;
		}
	}

}
