package dk.jonaslindstrom.mosef.modules;

public interface Module {

	/**
	 * Iterate the state of the module and return the output sound buffer.
	 * Input buffers should be left unchanged by a module.
	 * 
	 * @return
	 */
	public float[] getNextSamples();
	
}
