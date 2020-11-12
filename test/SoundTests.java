import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.melody.Track;
import dk.jonaslindstrom.mosef.modules.melody.VoiceBuilder;
import dk.jonaslindstrom.mosef.modules.melody.VoiceInputs;
import dk.jonaslindstrom.mosef.modules.melody.voices.ElectricPiano;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.WellTemperedTuningFunction;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.junit.Test;

public class SoundTests {

  public class SoundTest {

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
    new SoundTest(m -> m.whiteNoise()).runTest();
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
    new SoundTest(m -> m.square(m.arpeggio(480, new int[] {60, 64, 67}))).runTest();
  }

  @Test
  public void testRhythm() {
    new SoundTest(m -> m.amplifier(m.square(220),
        m.envelope(m.rhythm(480, new int[] {1, 0, 0, 1, 0, 0, 1, 0}), 0, 0, 1.0, 0.1)), 4).runTest();
  }

  @Test
  public void testFilterSweep() {
    new SoundTest(m -> m.vcf(m.amplifier(m.mixer(m.square(220), m.square(221), m.square(222)), 0.4), m.offset(m.sine(0.15), 500, 500)), 5).runTest();
  }

  @Test
  public void testSample() {
    new SoundTest(m -> m.sample(new File("piano.wav")), 4).runTest();
  }

  @Test
  public void testTrack() {
    Track track = new Track();
    track.addNote(60, 1.0, 1.0, 1.0);
    track.addNote(64, 2.0, 1.0, 1.0);
    track.addNote(67, 3.0, 1.0, 1.0);
    track.addNote(72, 4.0, 1.0, 1.0);

    new SoundTest(m -> new ElectricPiano().build(m, m.monophonicFromTrack(track)), 5).runTest();
  }

}
