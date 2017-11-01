package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.MOSEFSettings;

/**
 * This is an implementation of a module where the ouput at a specific sample
 * only depends on the corresponding sample of the input signals.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public abstract class SimpleModule implements Module {

	protected MOSEFSettings settings;
	private Module[] inputs;
	private float[] buffer;
	private float[][] inputBuffers;

	public SimpleModule(MOSEFSettings settings, Module ... inputs) {
		this.buffer = new float[settings.getBufferSize()];
		
		this.settings = settings;
		this.inputs = inputs;
		
		this.inputBuffers = new float[inputs.length][];	
	}
	
	/**
	 * Return the sample of the output given the the corresponding samples of
	 * the input signals. The order of the input signals are the same as given
	 * in the constructor.
	 * 
	 * @param inputs
	 * @return
	 */
	public abstract float getNextSample(float ... inputs);
	
	@Override
	public float[] getNextSamples() {
		
		for (int i = 0; i < inputs.length; i++) {
			inputBuffers[i] = inputs[i].getNextSamples();
		}
		
		for (int j = 0; j < settings.getBufferSize(); j++) {
			float[] current = new float[inputs.length];	
			
			for (int i = 0; i < inputs.length; i++) {
				current[i] = inputBuffers[i][j];
			}
			buffer[j] = getNextSample(current);
		}
				
		return buffer;
	}
}
