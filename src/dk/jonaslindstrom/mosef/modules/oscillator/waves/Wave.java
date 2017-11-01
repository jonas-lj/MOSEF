package dk.jonaslindstrom.mosef.modules.oscillator.waves;

/**
 * This interface represents a wave function, ie. a periodic function with
 * period 1 used to describe wave forms.
 * 
 * @author jonas
 *
 */
public interface Wave {
	
	/**
	 * Given time 0 \leq t < 1 this function returns the wave function for the
	 * given point in time.
	 * 
	 * @param t
	 * @return
	 */
	public float[] getSamples(float[] t);

}
