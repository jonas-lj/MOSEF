package dk.jonaslindstrom.mosef.modules.output;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.StoppableModule;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.apache.commons.math3.util.FastMath;

public class MonoOutput implements StoppableModule, OutputModule {

  private final Module module;
  private boolean running;
  private final MOSEFSettings settings;

  /**
   * Instances of this class takes an input module and outputs it through the sound card.
   */
  public MonoOutput(MOSEFSettings settings, Module input) {
    if (settings.getBitRate() != 16) {
      throw new UnsupportedOperationException("For now, only 16 bit output is allowed");
    }
    this.settings = settings;
    this.module = input;
  }

  @Override
  public void start() {

    final int byteRate = settings.getBitRate() / 8;

    AudioFormat audioFormat =
        new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1,
            true, true);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

    try {
      final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
      soundLine.open(audioFormat, settings.getBufferSize() * byteRate);

      soundLine.start();

      this.running = true;

      Thread synthThread = new Thread(() -> {
        ByteBuffer byteBuffer = ByteBuffer.allocate(settings.getBufferSize() * byteRate);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        double scale = FastMath.pow(2, settings.getBitRate() - 1);

        double[] buffer;

        while (running) {
          buffer = module.getNextSamples();
          if (buffer == null) {
            buffer = new double[settings.getBufferSize()];
          }

          shortBuffer.rewind();
          for (int i = 0; i < buffer.length; i++) {
            shortBuffer.put((short) (buffer[i] * scale));
          }
          byte[] bytes = byteBuffer.array();
          soundLine.write(bytes, 0, bytes.length);
        }
      });
      synthThread.start();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    this.running = false;
  }

}
