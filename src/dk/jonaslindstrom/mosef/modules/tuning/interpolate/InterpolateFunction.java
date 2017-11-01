package dk.jonaslindstrom.mosef.modules.tuning.interpolate;

public interface InterpolateFunction {

	/**
	 * When t = 0.0f this method should return 0.0f, and when t = 1.0f this method should return 1.0f.
	 * 
	 * @param t
	 * @return
	 */
	public float interpolate(float t);

}
