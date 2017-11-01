package dk.jonaslindstrom.mosef.modules.oscillator.waves.samplefunctions;

public interface SampleFunction {

	/**
	 * Return the value of the sample function in the point x in the interval
	 * [0,1].
	 * 
	 * @param x
	 */
	public float getValue(float x);

}
