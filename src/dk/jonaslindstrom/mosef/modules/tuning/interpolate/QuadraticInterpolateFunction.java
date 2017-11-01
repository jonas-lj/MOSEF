package dk.jonaslindstrom.mosef.modules.tuning.interpolate;

public class QuadraticInterpolateFunction implements InterpolateFunction {

	@Override
	public float interpolate(float t) {
		return t*t;
	}

}
