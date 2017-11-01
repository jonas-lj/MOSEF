package dk.jonaslindstrom.mosef.modules.filter.filters;



/**
 * This class represents a simple low-pass filter done by averaging the last
 * <i>n</i> samples, so <i>n</i> is the wave length of the cutoff frequency.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class MovingAverageFilterFactory implements DiscreteFilterFactory {

	private int length;

	public MovingAverageFilterFactory(int length) {
		this.length = length;
	}
	
	@Override
	public float[] getSamples() {
		float[] a = new float[length];
		for (int k = 0; k < length; k++) {
			a[k] = 1.0f / length;
		}
		return a;
	}

}
