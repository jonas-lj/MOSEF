package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public interface TuningFunction {

	/**
	 * A3 = 0.
	 * 
	 * @param note
	 * @return
	 */
	public float getFrequency(int note);
	
}
