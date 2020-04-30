package dk.jonaslindstrom.mosef.modules.output;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.StopableModule;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MonoOutput implements StopableModule, OutputModule {

  private Module module;
  private boolean running;
  private MOSEFSettings settings;

  /**
   * Instances of this class takes an input module and outputs it through the sound card.
   * 
   * @param settings
   * @param input
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
        new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1, true, true);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

    try {
      final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
      soundLine.open(audioFormat, settings.getBufferSize() * byteRate);

      soundLine.start();

      this.running = true;

      Thread synthThread = new Thread(() -> {
        ByteBuffer byteBuffer = ByteBuffer.allocate(settings.getBufferSize() * byteRate);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        double scale = Math.pow(2, settings.getBitRate() - 1);

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
          soundLine.write(byteBuffer.array(), 0, settings.getBufferSize() * byteRate);
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
