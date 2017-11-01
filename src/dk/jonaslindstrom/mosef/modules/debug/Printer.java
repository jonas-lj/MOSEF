package dk.jonaslindstrom.mosef.modules.debug;

import dk.jonaslindstrom.mosef.modules.Module;

public class Printer implements Module {

	private Module input;
	private int frequency;
	private int counter;

	/**
	 * Instances of this module replicates the input module and prints the value
	 * of each <i>frequency</i>th value to stdout.
	 * 
	 * @param input
	 * @param frequency
	 */
	public Printer(Module input, int frequency) {
		this.input = input;
		this.frequency = frequency;
		this.counter = 0;
	}
	
	@Override
	public float[] getNextSamples() {
		float[] buffer = input.getNextSamples();

		for (int i = 0; i < buffer.length; i++) {
			counter++;
			if (counter == frequency) {
				System.out.println(buffer[i]);
				counter = 0;
			}			
		}
		return buffer;
	}

}
