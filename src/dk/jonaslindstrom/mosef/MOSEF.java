package dk.jonaslindstrom.mosef;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.amplifier.Amplifier;
import dk.jonaslindstrom.mosef.modules.amplifier.Multiplier;
import dk.jonaslindstrom.mosef.modules.delay.Delay;
import dk.jonaslindstrom.mosef.modules.delay.Echo;
import dk.jonaslindstrom.mosef.modules.feedback.Feedback;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilter;
import dk.jonaslindstrom.mosef.modules.filter.LowPassFilterFixed;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.HammingWindow;
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
import dk.jonaslindstrom.mosef.modules.oscillator.waves.samplefunctions.SineFunction;
import dk.jonaslindstrom.mosef.modules.output.Output;
import dk.jonaslindstrom.mosef.modules.sample.Sample;
import dk.jonaslindstrom.mosef.modules.sample.SampleFactory;
import dk.jonaslindstrom.mosef.modules.splitter.Splitter;

public class MOSEF {

	private MOSEFSettings settings;

	public MOSEF(MOSEFSettings settings) {
		this.settings = settings;
	}
	
	public MOSEFSettings getSettings() {
		return this.settings;
	}

	/*
	 * Basic effects
	 */
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
	public Module amplifier(Module input, float level) {
		return new Amplifier(settings, input, constant(level));
	}

	public Module multiplier(Module input, Module multiplier) {
		return new Multiplier(settings, input, multiplier);
	}

	public Module multiplier(Module input, float multiplier) {
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
	
	/**
	 * Create a new module which always returns the same constant value.
	 * 
	 * @param value
	 * @return
	 */
	public Module constant(float value) {
		return new Constant(settings, value);
	}

	public Module[] constants(float... values) {
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
	public Module center(Module input, Module center, Module scale) {
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
	public Module center(Module input, float center, Module scale) {
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
	public Module center(Module input, Module center, float scale) {
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
	public Module center(Module input, float center, float scale) {
		return center(input, constant(center), constant(scale));
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

	/*
	 * Sample
	 */

	/**
	 * Create a sample buffer which returns the given samples one after the
	 * other.
	 * 
	 * @param sample
	 * @return
	 */
	public Module sample(float[] sample) {
		return new Sample(settings, sample);
	}

	/**
	 * Create a sample from a wave file (see also {@link #sample(float[])}.
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

	/*
	 * Feedback
	 */

	/**
	 * Create a feedback module which mixes feedback with the input module. When
	 * the source for the feedback is ready, it should be added to the module
	 * using the {@link Feedback#setFeedbackSource(Module)}.
	 * 
	 * @param input
	 * @return
	 */
	public Feedback feedback(Module input) {
		return new Feedback(settings, input);
	}

	/*
	 * Delay
	 */

	/**
	 * Create a module which delays the input module with a given delay. An
	 * upper bound for the delay should be specified to limit the amount of
	 * memory used by the module.
	 * 
	 * @param input
	 * @param delay
	 * @param maxDelay
	 * @return
	 */
	public Module delay(Module input, Module delay, float maxDelay) {
		return new Delay(settings, input, delay, maxDelay);
	}

	/**
	 * Create a module which delays the input module with a given delay.
	 * 
	 * @param input
	 * @param delay
	 * @return
	 */
	public Module delay(Module input, float delay) {
		return delay(input, constant(delay), delay);
	}


	/*
	 * Oscillators
	 */
	public Module sine(Module frequency) {
		return new Oscillator(settings, frequency, new SampledWave(settings, new SineFunction(), 256));
	}

	public Module sine(float frequency) {
		return sine(constant(frequency));
	}
	
	public Module square(Module frequency) {
		return new Oscillator(settings, frequency, new SquareWave(settings));
	}
	
	public Module square(float frequency) {
		return square(constant(frequency));
	}

	public Module triangle(Module frequency) {
		return new Oscillator(settings, frequency, new TriangleWave(settings));
	}

	public Module triangle(float frequency) {
		return triangle(constant(frequency));
	}
	
	public Module saw(Module frequency) {
		return new Oscillator(settings, frequency, new SawWave(settings));
	}

	public Module saw(float frequency) {
		return saw(constant(frequency));
	}
	
	public Module pulse(Module frequency, Module pulsewidth) {
		return new Oscillator(settings, frequency, new PulseWave(pulsewidth));
	}

	public Module pulse(float frequency, Module pulsewidth) {
		return pulse(constant(frequency), pulsewidth);
	}
	
	private static int[] overtoneRatios = new int[] { 2, 3, 4, 6, 8, 10, 12, 16 };

	public Module organ(Module baseFrequency, Wave wave, Module... levels) {
		int numberOfOvertones = levels.length;

		Module[] oscillators = new Module[numberOfOvertones + 1];

		Module[] splitFrequencies = split(baseFrequency, numberOfOvertones + 1);
		oscillators[0] = new Oscillator(settings, splitFrequencies[0], wave);

		for (int i = 1; i <= numberOfOvertones; i++) {
			oscillators[i] = amplifier(new Oscillator(settings,
					amplifier(splitFrequencies[i], overtoneRatios[i - 1]), wave), levels[i - 1]);
		}

		return amplifier(mixer(oscillators), constant(0.5f));
	}

	
	/*
	 * Filters
	 */
	
	public Module lowPassFilter(Module input, float cutoff) {
		return new LowPassFilterFixed(settings, input, cutoff, 512, new HammingWindow(101));
	}
	
	public Module lowPassFilter(Module input, Module cutoff) {
		return new LowPassFilter(settings, input, cutoff, 256, 512, new HammingWindow(101));
	}
	
	
	/*
	 * Modulation effects
	 */
	
	/**
	 * Create a chorus effect on the input with the given parameters.
	 * 
	 * @param input
	 * @param rate
	 * @param wetness
	 * @param depth
	 * @return
	 */
	public Module chorus(Module input, Module rate, Module wetness, Module depth) {
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
	public Module chorus(Module input, Module rate, Module wet) {
		return chorus(input, rate, wet, constant(0.004f)); // Default depth
	}

	/**
	 * Create an echo effect with <code>echos</code> number of echos with
	 * <code>time</code> between them.
	 * 
	 * @param input
	 * @param echos
	 * @param time
	 * @param max Max time between delays
	 * @return
	 */
	public Module echo(Module input, int echos, Module time, float max) {
		return new Echo(this, input, echos, time, max);
	}

	/**
	 * Create a simple ensemble effect which is made from three chorus effects
	 * with different rates.
	 * 
	 * @param input
	 * @return
	 */
	public Module ensemble(Module input) {
		return new Ensemble(this, input);
	}

	/**
	 * Create a reverb effect.
	 * 
	 * @param input
	 * @return
	 */
	public Module reverb(Module input) {
		return new Reverb(this, input);
}

	public Module vibrato(Module input, Module rate, Module depth, float maxDepth) {
		return new Vibrato(this, input, rate, depth);
	}

	public Module vibrato(Module input, Module rate, Module depth) {
		return vibrato(input, rate, depth, 0.75f);
	}
	
	
	/*
	 * Distortion/limiter effects
	 */
	public Module distortion(Module input, Module distortion) {
		return new Distortion(settings, input, distortion);
	}
	
	/*
	 * Misc
	 */

	public Module noise() {
		return new Noise(settings);
	}
	
	public Output output(Module input) {
		return new Output(settings, input);
	}
	
}
