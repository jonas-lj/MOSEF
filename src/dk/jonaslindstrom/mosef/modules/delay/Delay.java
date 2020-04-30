package dk.jonaslindstrom.mosef.modules.delay;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

/**
 * Instances of this class represents delay modules which takes an input signal and outputs a copy
 * of it delayed.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class Delay extends SimpleModule {

  private final SampleMemory sampleMemory;

  /**
   * Create a new delay module whose output is the input signal with its phase shifted.
   *
   * @param settings
   * @param input The module used for input.
   * @param delay The module determining how much delay should be applied to the input. If it
   *        outputs <i>s</i>, this module outputs the sample that was received from the input module
   *        <i>s x sampleRate</i> samples earlier.
   * @param memory Max delay time in seconds.
   */
  public Delay(MOSEFSettings settings, Module input, Module delay, double memory) {
    super(settings, input, delay);
    this.sampleMemory = new SampleMemory((int) (memory * settings.getSampleRate()));
  }

  @Override
  public double getNextSample(double... inputs) {

    sampleMemory.push(inputs[0]);

    int delayInSamples = (int) (inputs[1] * settings.getSampleRate());
    return sampleMemory.get(delayInSamples);
  }

}
