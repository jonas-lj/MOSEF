package dk.jonaslindstrom.mosef.modules.feedback;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.mixer.Mixer;
import java.util.Map;

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
public class Feedback implements MOSEFModule {

	private Mixer mixer;
	private MOSEFModule input;
	private MOSEFSettings settings;

	public Feedback(MOSEFSettings settings, MOSEFModule input) {
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
	public void setFeedbackSource(MOSEFModule feedback) {
		this.mixer = new Mixer(settings, input, feedback);
	}

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input);
  }

}
