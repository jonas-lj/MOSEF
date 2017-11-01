package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEF;

public abstract class CompositeModule implements Module {
	
	private MOSEF mosef;
	private Module output;
	
	public CompositeModule(MOSEF mosef) {
		this.mosef = mosef;
	}
	
	/**
	 * Build the composite module and return the output module.
	 * 
	 * @param m
	 * @return
	 */
	public abstract Module buildModule(MOSEF m);
	
	@Override
	public float[] getNextSamples() {
		if (output == null) {
			output = buildModule(mosef);
		}
		return output.getNextSamples();
	}

}
