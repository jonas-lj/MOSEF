package dk.jonaslindstrom.mosef.modules.sample;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleConverter;
import dk.jonaslindstrom.mosef.modules.Module;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SampleFactory {

  public static Sample fromFile(MOSEFSettings settings, File file, boolean wrap)
      throws IOException, UnsupportedAudioFileException {

    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

    SampleConverter converter =
        new SampleConverter(audioInputStream.getFormat().getSampleSizeInBits(),
            audioInputStream.getFormat().isBigEndian());

    byte[] audioBytes =
        new byte[settings.getBufferSize() * audioInputStream.getFormat().getFrameSize()];

    double[] buffer = new double[settings.getBufferSize()];
    DoubleBuffer frames = DoubleBuffer.allocate((int) audioInputStream.getFrameLength() * 2);

    while (audioInputStream.read(audioBytes) != -1) {
      converter.fillSamples(buffer, audioBytes);
      frames.put(buffer);
    }
    return new Sample(settings, frames.array(), wrap);
  }

  public static Sample fromModule(MOSEFSettings settings, Module input, int samples,
      boolean wrap) {
    DoubleBuffer buffer = DoubleBuffer.allocate(samples);
    for (int i = 0; i < samples / settings.getBufferSize(); i++) {
      buffer.put(input.getNextSamples());
    }
    return new Sample(settings, buffer.array(), wrap);
  }

}
