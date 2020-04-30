package dk.jonaslindstrom.mosef;

import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.StopableModule;
import dk.jonaslindstrom.mosef.modules.amplifier.Amplifier;
import dk.jonaslindstrom.mosef.modules.amplifier.Multiplier;
import dk.jonaslindstrom.mosef.modules.delay.Delay;
import dk.jonaslindstrom.mosef.modules.envelope.VCADSREnvelope;
import dk.jonaslindstrom.mosef.modules.envelope.ADSREnvelope;
import dk.jonaslindstrom.mosef.modules.feedback.Feedback;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilter;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilterFixed;
import dk.jonaslindstrom.mosef.modules.filter.filters.LPF;
import dk.jonaslindstrom.mosef.modules.filter.filters.VCF;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.HammingWindow;
import dk.jonaslindstrom.mosef.modules.input.Input;
import dk.jonaslindstrom.mosef.modules.limiter.Distortion;
import dk.jonaslindstrom.mosef.modules.limiter.Limiter;
import dk.jonaslindstrom.mosef.modules.misc.Constant;
import dk.jonaslindstrom.mosef.modules.mixer.Mixer;
import dk.jonaslindstrom.mosef.modules.modulation.Chorus;
import dk.jonaslindstrom.mosef.modules.modulation.Vibrato;
import dk.jonaslindstrom.mosef.modules.noise.Noise;
import dk.jonaslindstrom.mosef.modules.oscillator.ModulatedOscillator;
import dk.jonaslindstrom.mosef.modules.oscillator.Oscillator;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.MoogSquareWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.PulseWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SampledWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SawWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SquareWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.TriangleWave;
import dk.jonaslindstrom.mosef.modules.output.MonoOutput;
import dk.jonaslindstrom.mosef.modules.output.OutputModule;
import dk.jonaslindstrom.mosef.modules.output.StereoOutput;
import dk.jonaslindstrom.mosef.modules.sample.Sample;
import dk.jonaslindstrom.mosef.modules.sample.SampleFactory;
import dk.jonaslindstrom.mosef.modules.splitter.Splitter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MOSEF {

  private final MOSEFSettings settings;
  private List<StopableModule> stopableModules = new ArrayList<>();
  private OutputModule output;

  public MOSEF(MOSEFSettings settings) {
    this.settings = settings;
  }

  public MOSEFSettings getSettings() {
    return this.settings;
  }

  public Module build(Module input, DoubleUnaryOperator function) {
    return new SimpleModule(settings, input) {

      @Override
      public double getNextSample(double... inputs) {
        return function.applyAsDouble(inputs[0]);
      }

    };
  }

  public Module build(Module input1, Module input2, DoubleBinaryOperator function) {
    return new SimpleModule(settings, input1, input2) {

      @Override
      public double getNextSample(double... inputs) {
        return function.applyAsDouble(inputs[0], inputs[1]);
      }

    };
  }

  /**
   * Create a new module which amplifies the input with the given level.
   * 
   * @param input
   * @param level
   * @return
   */
  public Module amplifier(Module input, Module level) {
    return new Amplifier(settings, input, level);
  }

  /**
   * Create a new module which amplifies the input with the given level.
   * 
   * @param input
   * @param level
   * @return
   */
  public Module amplifier(Module input, double level) {
    return new Amplifier(settings, input, constant(level));
  }

  public Module multiplier(Module input, Module multiplier) {
    return new Multiplier(settings, input, multiplier);
  }

  public Module multiplier(Module input, double multiplier) {
    return new Multiplier(settings, input, constant(multiplier));
  }

  /**
   * Create a new module which mixes all the inputs.
   * 
   * @param inputs
   * @return
   */
  public Module mixer(Module... inputs) {
    return new Mixer(settings, inputs);
  }

  public Module mixer(List<Module> inputs) {
    return new Mixer(settings, inputs);
  }
  /**
   * Create a new module which always returns the same constant value.
   * 
   * @param value
   * @return
   */
  public Module constant(double value) {
    return new Constant(settings, value);
  }

  public Module[] constants(double... values) {
    Module[] constants = new Module[values.length];
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
  public Module offset(Module input, Module center, Module scale) {
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
  public Module offset(Module input, double center, Module scale) {
    return offset(input, constant(center), scale);
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public Module offset(Module input, Module center, double scale) {
    return offset(input, center, constant(scale));
  }

  /**
   * Map the input signal to input * scale + center.
   * 
   * @param input
   * @param center
   * @param scale
   * @return
   */
  public Module offset(Module input, double center, double scale) {
    return offset(input, constant(center), constant(scale));
  }

  /**
   * Create <code>n</code> new modules which are all copies of the input.
   * 
   * @param input
   * @param n
   * @return
   */
  public Module[] split(Module input, int n) {
    return Splitter.split(input, n);
  }

  /**
   * Create a new module which limits the amplitude of the input module.
   * 
   * @param input
   * @param limit
   * @return
   */
  public Module limiter(Module input, Module limit) {
    return new Limiter(settings, input, limit);
  }

  /**
   * Create a sample buffer which returns the given samples one after the other.
   * 
   * @param sample
   * @return
   */
  public Module sample(double[] sample) {
    return new Sample(settings, sample);
  }

  /**
   * Create a sample from a wave file (see also {@link #sample(double[])}.
   * 
   * @param file
   * @return
   */
  public Module sample(File file) {
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
   * {@link Feedback#attachFeedback(Module)}.
   * 
   * @param input
   * @return
   */
  public Feedback feedback(Module input, Module rate) {
    return new Feedback(settings, input, rate);
  }

  public Feedback feedback(Module input, double rate) {
    return new Feedback(settings, input, constant(rate));
  }

  public Module envelope(Module gate, double a, double d, double s, double r) {
    return new ADSREnvelope(settings, gate, a, d, s, r);
  }

  public Module envelope(Module gate, Module a, Module d, Module s, Module r) {
    return new VCADSREnvelope(settings, gate, a, d, s, r);
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
  public Module delay(Module input, Module delay, double maxDelay) {
    return new Delay(settings, input, delay, maxDelay);
  }

  /**
   * Create a module which delays the input module with a given delay.
   * 
   * @param input
   * @param delay
   * @return
   */
  public Module delay(Module input, double delay) {
    return delay(input, constant(delay), delay+1);
  }

  public Module sine(Module frequency) {
    return new Oscillator(settings, frequency,
        new SampledWave(t -> (double) Math.sin(2.0 * Math.PI * t), 256));
  }

  public Module sine(double frequency) {
    return sine(constant(frequency));
  }

  public Module square(Module frequency) {
    return new Oscillator(settings, frequency, new MoogSquareWave());
  }

  public Module square(double frequency) {
    return square(constant(frequency));
  }

  public Module triangle(Module frequency) {
    return new Oscillator(settings, frequency, new TriangleWave());
  }

  public Module triangle(double frequency) {
    return triangle(constant(frequency));
  }

  public Module saw(Module frequency) {
    return new Oscillator(settings, frequency, new SawWave());
  }

  public Module saw(double frequency) {
    return saw(constant(frequency));
  }

  public Module pulse(Module frequency, Module pulsewidth) {
    return new ModulatedOscillator(settings, frequency, pulsewidth, new PulseWave());
  }

  public Module pulse(double frequency, Module pulsewidth) {
    return pulse(constant(frequency), pulsewidth);
  }

  private static int[] overtoneRatios = new int[] {2, 3, 4, 6, 8, 10, 12, 16};

  public Module organ(Module baseFrequency, DoubleUnaryOperator wave, Module... levels) {
    int numberOfOvertones = levels.length;

    Module[] oscillators = new Module[numberOfOvertones + 1];

    Module[] splitFrequencies = split(baseFrequency, numberOfOvertones + 1);
    oscillators[0] = new Oscillator(settings, splitFrequencies[0], wave);

    for (int i = 1; i <= numberOfOvertones; i++) {
      oscillators[i] = amplifier(
          new Oscillator(settings, amplifier(splitFrequencies[i], overtoneRatios[i - 1]), wave),
          levels[i - 1]);
    }

    return amplifier(mixer(oscillators), constant(0.5f));
  }

  public Module filter(Module input, double cutoff) {
    return new LPF(settings, input, cutoff);
  }

  public Module vcf(Module input, Module cutoff) {
    return new VCF(settings, input, cutoff);
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
  public Module chorus(Module input, Module rate, Module wetness,
      Module depth) {
    return new Chorus(settings, input, rate, wetness, depth);
  }

  /**
   * Create a chorus effect on the input with the given parameters.
   * 
   * @param input
   * @param rate
   * @param wet
   * @return
   */
  public Module chorus(Module input, Module rate, Module wet) {
    return chorus(input, rate, wet, constant(0.004f)); // Default depth
  }

  public Module vibrato(Module input, Module rate, Module depth,
      double maxDepth) {
    return new Vibrato(settings, input, rate, depth);
  }

  public Module vibrato(Module input, Module rate, Module depth) {
    return vibrato(input, rate, depth, 0.75f);
  }

  public Module distortion(Module input, Module distortion) {
    return new Distortion(settings, input, distortion);
  }

  public Module noise() {
    return new Noise(settings);
  }

  public void audioOut(Module input) {
    MonoOutput output = new MonoOutput(settings, input);
    this.output = output;
    stopableModules.add(output);
  }

  public void audioOut(Module left, Module right) {
    StereoOutput output = new StereoOutput(settings, left, right);
    this.output = output;
    stopableModules.add(output);
  }

  public Module audioIn() {
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
