package dk.jonaslindstrom.mosef;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.StopableModule;
import dk.jonaslindstrom.mosef.modules.amplifier.Amplifier;
import dk.jonaslindstrom.mosef.modules.amplifier.Multiplier;
import dk.jonaslindstrom.mosef.modules.delay.Delay;
import dk.jonaslindstrom.mosef.modules.delay.Echo;
import dk.jonaslindstrom.mosef.modules.feedback.Feedback;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilter;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilterFixed;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.HammingWindow;
import dk.jonaslindstrom.mosef.modules.input.Input;
import dk.jonaslindstrom.mosef.modules.limiter.Distortion;
import dk.jonaslindstrom.mosef.modules.limiter.Limiter;
import dk.jonaslindstrom.mosef.modules.misc.Constant;
import dk.jonaslindstrom.mosef.modules.mixer.Mixer;
import dk.jonaslindstrom.mosef.modules.modulation.Chorus;
import dk.jonaslindstrom.mosef.modules.modulation.Ensemble;
import dk.jonaslindstrom.mosef.modules.modulation.Reverb;
import dk.jonaslindstrom.mosef.modules.modulation.Vibrato;
import dk.jonaslindstrom.mosef.modules.noise.Noise;
import dk.jonaslindstrom.mosef.modules.oscillator.Oscillator;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.PulseWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SampledWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SawWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SquareWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.TriangleWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.Wave;
import dk.jonaslindstrom.mosef.modules.output.Output;
import dk.jonaslindstrom.mosef.modules.output.OutputModule;
import dk.jonaslindstrom.mosef.modules.sample.Sample;
import dk.jonaslindstrom.mosef.modules.sample.SampleFactory;
import dk.jonaslindstrom.mosef.modules.splitter.Splitter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MOSEF {

  private MOSEFSettings settings;
  private List<StopableModule> stopableModules = new ArrayList<>();
  private OutputModule output;

  public MOSEF(MOSEFSettings settings) {
    this.settings = settings;
  }

  public MOSEFSettings getSettings() {
    return this.settings;
  }

  /**
   * Create a new module which amplifies the input with the given level.
   * 
   * @param input
   * @param level
   * @return
   */
  public MOSEFModule amplifier(MOSEFModule input, MOSEFModule level) {
    return new Amplifier(settings, input, level);
  }

  /**
   * Create a new module which amplifies the input with the given level.
   * 
   * @param input
   * @param level
   * @return
   */
  public MOSEFModule amplifier(MOSEFModule input, float level) {
    return new Amplifier(settings, input, constant(level));
  }

  public MOSEFModule multiplier(MOSEFModule input, MOSEFModule multiplier) {
    return new Multiplier(settings, input, multiplier);
  }

  public MOSEFModule multiplier(MOSEFModule input, float multiplier) {
    return new Multiplier(settings, input, constant(multiplier));
  }

  /**
   * Create a new module which mixes all the inputs.
   * 
   * @param inputs
   * @return
   */
  public MOSEFModule mixer(MOSEFModule... inputs) {
    return new Mixer(settings, inputs);
  }

  /**
   * Create a new module which always returns the same constant value.
   * 
   * @param value
   * @return
   */
  public MOSEFModule constant(float value) {
    return new Constant(settings, value);
  }

  public MOSEFModule[] constants(float... values) {
    MOSEFModule[] constants = new MOSEFModule[values.length];
    for (int i = 0; i < values.length; i++) {
      constants[i] = constant(values[i]);
    }
    return constants;
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public MOSEFModule center(MOSEFModule input, MOSEFModule center, MOSEFModule scale) {
    return mixer(amplifier(input, scale), center);
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public MOSEFModule center(MOSEFModule input, float center, MOSEFModule scale) {
    return center(input, constant(center), scale);
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public MOSEFModule center(MOSEFModule input, MOSEFModule center, float scale) {
    return center(input, center, constant(scale));
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public MOSEFModule center(MOSEFModule input, float center, float scale) {
    return center(input, constant(center), constant(scale));
  }

  /**
   * Create <code>n</code> new modules which are all copies of the input.
   * 
   * @param input
   * @param n
   * @return
   */
  public MOSEFModule[] split(MOSEFModule input, int n) {
    return Splitter.split(input, n);
  }

  /**
   * Create a new module which limits the amplitude of the input module.
   * 
   * @param input
   * @param limit
   * @return
   */
  public MOSEFModule limiter(MOSEFModule input, MOSEFModule limit) {
    return new Limiter(settings, input, limit);
  }

  /**
   * Create a sample buffer which returns the given samples one after the other.
   * 
   * @param sample
   * @return
   */
  public MOSEFModule sample(float[] sample) {
    return new Sample(settings, sample);
  }

  /**
   * Create a sample from a wave file (see also {@link #sample(float[])}.
   * 
   * @param file
   * @return
   */
  public MOSEFModule sample(File file) {
    try {
      return SampleFactory.fromFile(settings, file, false);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create a feedback module which mixes feedback with the input module. When the source for the
   * feedback is ready, it should be added to the module using the
   * {@link Feedback#setFeedbackSource(MOSEFModule)}.
   * 
   * @param input
   * @return
   */
  public Feedback feedback(MOSEFModule input) {
    return new Feedback(settings, input);
  }

  /**
   * Create a module which delays the input module with a given delay. An upper bound for the delay
   * should be specified to limit the amount of memory used by the module.
   * 
   * @param input
   * @param delay
   * @param maxDelay
   * @return
   */
  public MOSEFModule delay(MOSEFModule input, MOSEFModule delay, float maxDelay) {
    return new Delay(settings, input, delay, maxDelay);
  }

  /**
   * Create a module which delays the input module with a given delay.
   * 
   * @param input
   * @param delay
   * @return
   */
  public MOSEFModule delay(MOSEFModule input, float delay) {
    return delay(input, constant(delay), delay);
  }
  
  public MOSEFModule sine(MOSEFModule frequency) {
    return new Oscillator(settings, frequency,
        new SampledWave(settings, t -> (float) Math.sin(2 * (float) Math.PI * t), 256));
  }

  public MOSEFModule sine(float frequency) {
    return sine(constant(frequency));
  }

  public MOSEFModule square(MOSEFModule frequency) {
    return new Oscillator(settings, frequency, new SquareWave(settings));
  }

  public MOSEFModule square(float frequency) {
    return square(constant(frequency));
  }

  public MOSEFModule triangle(MOSEFModule frequency) {
    return new Oscillator(settings, frequency, new TriangleWave(settings));
  }

  public MOSEFModule triangle(float frequency) {
    return triangle(constant(frequency));
  }

  public MOSEFModule saw(MOSEFModule frequency) {
    return new Oscillator(settings, frequency, new SawWave(settings));
  }

  public MOSEFModule saw(float frequency) {
    return saw(constant(frequency));
  }

  public MOSEFModule pulse(MOSEFModule frequency, MOSEFModule pulsewidth) {
    return new Oscillator(settings, frequency, new PulseWave(pulsewidth));
  }

  public MOSEFModule pulse(float frequency, MOSEFModule pulsewidth) {
    return pulse(constant(frequency), pulsewidth);
  }

  private static int[] overtoneRatios = new int[] {2, 3, 4, 6, 8, 10, 12, 16};

  public MOSEFModule organ(MOSEFModule baseFrequency, Wave wave, MOSEFModule... levels) {
    int numberOfOvertones = levels.length;

    MOSEFModule[] oscillators = new MOSEFModule[numberOfOvertones + 1];

    MOSEFModule[] splitFrequencies = split(baseFrequency, numberOfOvertones + 1);
    oscillators[0] = new Oscillator(settings, splitFrequencies[0], wave);

    for (int i = 1; i <= numberOfOvertones; i++) {
      oscillators[i] = amplifier(
          new Oscillator(settings, amplifier(splitFrequencies[i], overtoneRatios[i - 1]), wave),
          levels[i - 1]);
    }

    return amplifier(mixer(oscillators), constant(0.5f));
  }

  public MOSEFModule lowPassFilter(MOSEFModule input, float cutoff) {
    return new LowPassFilterFixed(settings, input, cutoff, 512, new HammingWindow(101));
  }

  public MOSEFModule lowPassFilter(MOSEFModule input, MOSEFModule cutoff) {
    return new LowPassFilter(settings, input, cutoff, 256, 512, new HammingWindow(101));
  }

  /**
   * Create a chorus effect on the input with the given parameters.
   * 
   * @param input
   * @param rate
   * @param wetness
   * @param depth
   * @return
   */
  public MOSEFModule chorus(MOSEFModule input, MOSEFModule rate, MOSEFModule wetness, MOSEFModule depth) {
    return new Chorus(this, input, rate, wetness, depth);
  }

  /**
   * Create a chorus effect on the input with the given parameters.
   * 
   * @param input
   * @param rate
   * @param wetness
   * @param depth
   * @return
   */
  public MOSEFModule chorus(MOSEFModule input, MOSEFModule rate, MOSEFModule wet) {
    return chorus(input, rate, wet, constant(0.004f)); // Default depth
  }

  /**
   * Create an echo effect with <code>echos</code> number of echos with <code>time</code> between
   * them.
   * 
   * @param input
   * @param echos
   * @param time
   * @param max Max time between delays
   * @return
   */
  public MOSEFModule echo(MOSEFModule input, int echos, MOSEFModule time, float max) {
    return new Echo(this, input, echos, time, max);
  }

  /**
   * Create a simple ensemble effect which is made from three chorus effects with different rates.
   * 
   * @param input
   * @return
   */
  public MOSEFModule ensemble(MOSEFModule input) {
    return new Ensemble(this, input);
  }

  /**
   * Create a reverb effect.
   * 
   * @param input
   * @return
   */
  public MOSEFModule reverb(MOSEFModule input) {
    return new Reverb(this, input);
  }

  public MOSEFModule vibrato(MOSEFModule input, MOSEFModule rate, MOSEFModule depth, float maxDepth) {
    return new Vibrato(this, input, rate, depth);
  }

  public MOSEFModule vibrato(MOSEFModule input, MOSEFModule rate, MOSEFModule depth) {
    return vibrato(input, rate, depth, 0.75f);
  }

  public MOSEFModule distortion(MOSEFModule input, MOSEFModule distortion) {
    return new Distortion(settings, input, distortion);
  }

  public MOSEFModule noise() {
    return new Noise(settings);
  }

  public void audioOut(MOSEFModule input) {
    Output output = new Output(settings, input);
    this.output = output;
    stopableModules.add(output);
  }

  public MOSEFModule audioIn() {
    Input input = new Input(settings);
    stopableModules.add(input);
    return input;
  }

  public void stop() {
    for (StopableModule module : stopableModules) {
      module.stop();
    }
  }

  public void start() {
    if (this.output == null) {
      throw new IllegalStateException("No output module");
    }
    this.output.start();
  }

}
