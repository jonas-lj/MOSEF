package dk.jonaslindstrom.mosef.modules.feedback;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

/**
 * This class represents a module which allows us to provide feedback from a module which is not
 * defined until later. It is done as follows:
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
 */
public class Feedback implements Module {

  private final Module input, rate;
  private double[] buffer;
  private Module feedback;
  private final MOSEFSettings settings;

  public Feedback(MOSEFSettings settings, Module input, Module rate) {
    this.settings = settings;
    this.input = input;
    this.rate = rate;
    this.buffer = new double[settings.getBufferSize()];
  }

  /**
   * Set the input of this feedback module.
   *
   * @param input
   */
  public Module attachFeedback(Module input) {
    this.feedback = new FeedbackPlug(input);
    return feedback;
  }

  @Override
  public double[] getNextSamples() {
    buffer = input.getNextSamples();
    return buffer;
  }

  private class FeedbackPlug implements Module {

    private final Module input;

    private FeedbackPlug(Module input) {
      this.input = input;
    }

    @Override
    public double[] getNextSamples() {
      double[] in = input.getNextSamples();
      double[] amp = rate.getNextSamples();

      for (int i = 0; i < buffer.length; i++) {
        buffer[i] = in[i] + amp[i] * buffer[i];
      }

      return buffer;
    }

  }

}
