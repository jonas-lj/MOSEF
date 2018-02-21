package dk.jonaslindstrom.mosef.modules.input;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.StopableModule;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Input implements Module, StopableModule {

  private MOSEFSettings settings;
  private TargetDataLine targetLine;
  private int byterate;
  private int bufferSize;
  private Queue<float[]> buffers;
  private boolean running = false;
  private Thread inputThread;

  /**
   * Instances of this module provide sound from input sources, eg. instruments or microphones. This
   * module uses the default sound input source.
   * 
   * @param samplerate Number of samples per second, eg. 44100.
   * @param bitrate Bits per sample, eg. 16.
   * @param buffersize Size of the buffer used for this input source. Low buffersize may decrease
   *        latency but may cause stutter if the buffer runs out of data.
   */
  public Input(MOSEFSettings settings) {
    this.settings = settings;

    try {
      // Open the default input source
      boolean bigEndian = true;

      AudioFormat format =
          new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1, true, bigEndian);
      this.byterate = settings.getBitRate() / 8;
      this.bufferSize = settings.getBufferSize() * byterate;
      
      DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
      targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
      targetLine.open(format, bufferSize);      
      this.buffers = new ArrayBlockingQueue<>(3);

      Runnable runner = new Runnable() {
        @Override
        public void run() {
          ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
          ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
          float scale = (float) Math.pow(2, -settings.getBitRate() - 1);
          byte[] bytes = new byte[bufferSize];
          float[] buffer = new float[settings.getBufferSize()];
          
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
  public float[] getNextSamples() { 
    if (!running) {
      running = true;
      targetLine.start();
      inputThread.start();
    }
    
    if (buffers.isEmpty()) {
      return new float[settings.getBufferSize()];
    } else {
      return buffers.poll();
    }
  }
  
  @Override
  public void stop() {
    running = false;
  }

}