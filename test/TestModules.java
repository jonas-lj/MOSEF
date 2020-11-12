import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.amplifier.Amplifier;
import dk.jonaslindstrom.mosef.modules.delay.Delay;
import dk.jonaslindstrom.mosef.modules.feedback.Feedback;
import dk.jonaslindstrom.mosef.modules.misc.Constant;
import dk.jonaslindstrom.mosef.modules.mixer.Mixer;
import dk.jonaslindstrom.mosef.modules.oscillator.LFO;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SampledWave;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.SquareWave;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class TestModules {

  @Test
  public void testAmplifier() {
    int samples = 8;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);

    double input = 5.0;
    double level = 0.5;
    Constant constant = new Constant(settings, 5.0);
    Amplifier amplifier = new Amplifier(settings, constant, 0.5);

    double[] actual = amplifier.getNextSamples();
    double[] expected = new double[samples];
    Arrays.fill(expected, input * level);

    Assert.assertArrayEquals(expected, actual, Double.MIN_NORMAL);
  }

  @Test
  public void testConstant() {
    int samples = 8;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);

    double input = 5.0;
    Constant constant = new Constant(settings, 5.0);

    double[] actual = constant.getNextSamples();
    double[] expected = new double[samples];
    Arrays.fill(expected, input);

    Assert.assertArrayEquals(expected, actual, Double.MIN_NORMAL);
  }

  @Test
  public void testSquare() {
    int samples = 8;
    int sampleRate = 8;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);
    when(settings.getSampleRate()).thenReturn(sampleRate);

    LFO square = new LFO(settings, 2, new SquareWave());

    double[] actual = square.getNextSamples();
    // On first iteration, we take a step before we sample, so the phase is 1 / sampleRate
    double[] expected = new double[]{1, -1, -1, 1, 1, -1, -1, 1};

    Assert.assertArrayEquals(expected, actual, Double.MIN_NORMAL);
  }

  @Test
  public void testSine() {
    int samples = 512;
    int sampleRate = 512;
    double frequency = 3.2;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);
    when(settings.getSampleRate()).thenReturn(sampleRate);

    LFO sine = new LFO(settings, frequency, new SampledWave(t -> FastMath
        .sin(2.0 * Math.PI * t), 32));

    double[] actual = sine.getNextSamples();
    double[] expected = DoubleStream.iterate(0.0, t -> t + 2.0 * Math.PI * frequency / sampleRate).limit(samples).map(Math::sin).toArray();

    Assert.assertArrayEquals(expected, actual, 0.01);
  }

  @Test
  public void testMixer() {
    int samples = 4;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);

    Module a = mock(Module.class);
    when(a.getNextSamples()).thenReturn(new double[] {1,2,3,4});
    Module b = mock(Module.class);
    when(b.getNextSamples()).thenReturn(new double[] {4,5,6,7});

    Mixer mixer = new Mixer(settings, a, b);

    double[] actual = mixer.getNextSamples();
    double[] expected = new double[] {5, 7, 9, 11};

    Assert.assertArrayEquals(expected, actual, Double.MIN_VALUE);
  }

  @Test
  public void testDelay() {
    int samples = 8;
    int sampleRate = 8;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);
    when(settings.getSampleRate()).thenReturn(sampleRate);

    Module a = mock(Module.class);
    when(a.getNextSamples()).thenReturn(new double[] {1, 2, 3, 4, 5, 0, 0, 0});

    Delay delay = new Delay(settings, a, new Constant(settings,0.5), 1.0);

    double[] actual = delay.getNextSamples();
    double[] expected = new double[] {0, 0, 0, 0, 1, 2, 3, 4};

    Assert.assertArrayEquals(expected, actual, Double.MIN_VALUE);

    actual = delay.getNextSamples();
    expected = new double[] {5, 0, 0, 0, 1, 2, 3, 4};

    Assert.assertArrayEquals(expected, actual, Double.MIN_VALUE);
  }

  @Test
  public void testFeedback() {
    int samples = 4;
    MOSEFSettings settings = mock(MOSEFSettings.class);
    when(settings.getBufferSize()).thenReturn(samples);

    Module a = mock(Module.class);
    when(a.getNextSamples()).thenReturn(new double[] {1, 2, 3, 4});

    Feedback feedback = new Feedback(settings, a, new Constant(settings, 0.5));

    Module b = mock(Module.class);
    when(b.getNextSamples()).thenReturn(new double[] {0, 0, 1, 1});
    Module mixer = new Mixer(settings, feedback, b);

    Module withFeedback = feedback.attachFeedback(mixer);

    double[] actual = withFeedback.getNextSamples();
    double[] expected = new double[] {1.5, 3.0, 5.5, 7.0};

    Assert.assertArrayEquals(expected, actual, Double.MIN_VALUE);
  }

}
