import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.limiter.WaveFolder;
import dk.jonaslindstrom.mosef.modules.melody.Track;
import dk.jonaslindstrom.mosef.modules.patches.EastCoast;
import dk.jonaslindstrom.mosef.modules.patches.ElectricPiano;
import dk.jonaslindstrom.mosef.modules.polyphony.MonophonicVoice;
import dk.jonaslindstrom.mosef.modules.polyphony.PolyphonicStrategy;
import dk.jonaslindstrom.mosef.modules.polyphony.PolyphonicVoice;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.examples.EquallyTemperedTuningFunction;
import java.io.File;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.function.Function;
import org.junit.Test;

public class SoundTests {

  public static class SoundTest {

    private final long durationInSeconds;
    private final Function<MOSEF, Module> supplier;

    public SoundTest(Function<MOSEF, Module> supplier, long durationInSeconds) {
      this.supplier = supplier;
      this.durationInSeconds = durationInSeconds;
    }

    public SoundTest(Function<MOSEF, Module> supplier) {
      this(supplier,2);
    }

    public void runTest() {
      MOSEF m = new MOSEF(new MOSEFSettings(44100, 512, 16));
      Module out = supplier.apply(m);
      m.audioOut(m.amplifier(out, 0.5));

      m.start();
      try {
        TimeUnit.SECONDS.sleep(durationInSeconds);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        m.stop();
      }
    }
  }

  @Test
  public void testWhiteNoise() {
    new SoundTest(MOSEF::whiteNoise).runTest();
  }

  @Test
  public void testSine() {
    new SoundTest(m -> m.sine(220)).runTest();
  }

  @Test
  public void testSquare() {
    new SoundTest(m -> m.square(220)).runTest();
  }

  @Test
  public void testSaw() {
    new SoundTest(m -> m.saw(220)).runTest();
  }

  @Test
  public void testTriangle() {
    new SoundTest(m -> m.triangle(220)).runTest();
  }

  @Test
  public void testPulse() {
    new SoundTest(m -> m.pulse(110, m.offset(m.sine(1.0), 0.25, 0.1))).runTest();
  }

  @Test
  public void testArpeggio() {
    new SoundTest(m -> m.square(m.arpeggio(480, new int[]{60, 64, 67}))).runTest();
  }

  @Test
  public void testRhythm() {
    new SoundTest(m -> m.amplifier(m.square(220),
        m.envelope(m.rhythm(480, new int[]{1, 0, 0, 1, 0, 0, 1, 0}), 0, 0, 1.0, 0.1)), 4).runTest();
  }

  @Test
  public void testFilterSweep() {
    new SoundTest(m -> m.vcf(m.amplifier(m.mixer(m.square(220), m.square(221), m.square(222)), 0.4),
        m.offset(m.sine(0.35), 500, 500)), 15).runTest();
  }

  @Test
  public void testSample() {
    new SoundTest(m -> m.sample(new File("piano.wav")), 4).runTest();
  }

  @Test
  public void testTrack() {
    Track track = new Track();
    for (int i = 0; i < 10; i++) {
      track.addNote(70, i + 0.0, 1.0, 0.1);
      track.addNote(73, i + 0.25, 1.0, 0.1);
      track.addNote(77, i + 0.5, 1.0, 0.1);
      track.addNote(73, i + 0.75, 1.0, 0.1);
    }

    new SoundTest(m -> new EastCoast().build(m, m.monophonicFromTrack(track)), 10).runTest();
  }

  @Test
  public void testMonophonicVoice() {
    new SoundTest(m -> {
      MonophonicVoice voice = new MonophonicVoice(m, new EquallyTemperedTuningFunction(),
          (mosef, inputs) -> new ElectricPiano().build(mosef, inputs));
      voice.noteOn(58, 1.0);

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          voice.noteOff(58);
          voice.noteOn(70, 1.0);
        }
      }, 1000);
      return voice.getOutput();
    }, 2).runTest();
  }

  @Test
  public void testPolyphonicVoice() {
    new SoundTest(m -> {
      PolyphonicVoice voice = new PolyphonicVoice(m, 3, new EquallyTemperedTuningFunction(),
          (mosef, inputs) -> new ElectricPiano().build(mosef, inputs), PolyphonicStrategy.IGNORE);
      voice.noteOn(60, 1.0);
      voice.noteOn(64, 1.0);
      voice.noteOn(67, 1.0);

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          voice.noteOff(60);
          voice.noteOff(64);
          voice.noteOff(67);
          voice.noteOn(60, 1.0);
          voice.noteOn(65, 1.0);
          voice.noteOn(69, 1.0);
        }
      }, 1000);

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          voice.noteOff(60);
          voice.noteOff(65);
          voice.noteOff(69);
          voice.noteOn(60, 1.0);
          voice.noteOn(64, 1.0);
          voice.noteOn(67, 1.0);
        }
      }, 2000);

      return voice.getOutput();
    }, 4).runTest();
  }

  @Test
  public void testWaveFolder() {
    new SoundTest(m -> new WaveFolder(m.getSettings(), m.sine(220))).runTest();
  }

}
