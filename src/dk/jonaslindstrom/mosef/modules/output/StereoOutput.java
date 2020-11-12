package dk.jonaslindstrom.mosef.modules.output;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.StoppableModule;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.apache.commons.math3.util.FastMath;

public class StereoOutput implements StoppableModule, OutputModule {

  private final Module left;
  private final Module right;
  private final MOSEFSettings settings;
  private boolean running;

  public StereoOutput(MOSEFSettings settings, Module left, Module right) {
    if (settings.getBitRate() != 16) {
      throw new UnsupportedOperationException("For now, only 16 bit output is allowed");
    }
    this.settings = settings;
    this.left = left;
    this.right = right;
    this.running = false;
  }

  @Override
  public void stop() {
    running = false;
  }

  @Override
  public void start() {

    final int byteRate = settings.getBitRate() / 8;

    AudioFormat audioFormat =
        new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 2, true, true);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

    try {
      final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
      soundLine.open(audioFormat, 2 * settings.getBufferSize() * byteRate);

      soundLine.start();

      this.running = true;

      Thread synthThread = new Thread(() -> {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2 * settings.getBufferSize() * byteRate);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        double scale = FastMath.pow(2, settings.getBitRate() - 1);

        double[] leftBuffer = new double[settings.getBufferSize()];
        double[] rightBuffer = new double[settings.getBufferSize()];

        while (running) {
          leftBuffer = Objects
              .requireNonNullElse(left.getNextSamples(), leftBuffer);
          rightBuffer = Objects
              .requireNonNullElse(right.getNextSamples(), rightBuffer);

          shortBuffer.rewind();
          for (int i = 0; i < settings.getBufferSize(); i++) {
            shortBuffer.put((short) (leftBuffer[i] * scale));
            shortBuffer.put((short) (rightBuffer[i] * scale));
          }
          soundLine.write(byteBuffer.array(), 0, 2 * settings.getBufferSize() * byteRate);
        }
      });
      synthThread.start();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
}
