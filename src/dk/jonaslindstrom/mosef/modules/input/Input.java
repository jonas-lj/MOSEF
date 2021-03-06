package dk.jonaslindstrom.mosef.modules.input;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.StoppableModule;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import org.apache.commons.math3.util.FastMath;

public class Input implements Module, StoppableModule {

  private final MOSEFSettings settings;
  private TargetDataLine targetLine;
  private int bufferSize;
  private Queue<double[]> buffers;
  private boolean running = false;
  private Thread inputThread;

  /**
   * Instances of this module provide sound from input sources, eg. instruments or microphones. This
   * module uses the default sound input source.
   *
   * @param settings
   */
  public Input(MOSEFSettings settings) {
    this.settings = settings;

    try {
      AudioFormat format =
          new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1, true, true);
      int byterate = settings.getBitRate() / 8;
      this.bufferSize = settings.getBufferSize() * byterate;
      targetLine = AudioSystem.getTargetDataLine(format);
      targetLine.open(format, bufferSize);
      this.buffers = new ArrayBlockingQueue<>(3);

      Runnable runner = new Runnable() {
        @Override
        public void run() {
          ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
          ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
          double scale = FastMath.pow(2, -settings.getBitRate() - 1);
          byte[] bytes = new byte[bufferSize];
          double[] buffer = new double[settings.getBufferSize()];

          while (running) {
            targetLine.read(bytes, 0, bytes.length);
            byteBuffer.rewind();
            byteBuffer.put(bytes);
            shortBuffer.rewind();
            for (int i = 0; i < settings.getBufferSize(); i++) {
              buffer[i] = shortBuffer.get(i) * scale;
            }
            buffers.add(buffer);
          }
          targetLine.stop();
        }
      };
      this.inputThread = new Thread(runner);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  @Override
  public double[] getNextSamples() {
    if (!running) {
      running = true;
      targetLine.start();
      inputThread.start();
    }

    if (buffers.isEmpty()) {
      return new double[settings.getBufferSize()];
    } else {
      return buffers.poll();
    }
  }

  @Override
  public void stop() {
    running = false;
  }

}
