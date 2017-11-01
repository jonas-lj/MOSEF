package dk.jonaslindstrom.mosef.modules.feedback;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.mixer.Mixer;

/**
 * This class represents a module which allows us to provide feedback from a
 * module which is not defined until later. It is done as follows:
 * 
 * <pre>
 * ...
 * Feedback feedback = new Feedback(input);
 * ...
 * feedback.setFeedbackSource(source);
 * ...
 * </pre>
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Feedback implements Module {

	private Mixer mixer;
	private Module input;
	private MOSEFSettings settings;

	public Feedback(MOSEFSettings settings, Module input) {
		this.settings = settings;
		this.input = input;
	}

	@Override
	public float[] getNextSamples() {
		return mixer.getNextSamples();
	}

	/**
	 * Set the input of this feedback module.
	 * 
	 * @param feedback
	 */
	public void setFeedbackSource(Module feedback) {
		this.mixer = new Mixer(settings, input, feedback);
	}

}
