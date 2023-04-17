package dk.jonaslindstrom.mosef.modules.output;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import org.apache.commons.math3.util.FastMath;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class ToWaveFile {

    private final MOSEFSettings settings;
    private final Module module;
    private final File file;

    public ToWaveFile(MOSEFSettings settings, Module input, File file) {
        if (settings.getBitRate() != 16) {
            throw new UnsupportedOperationException("For now, only 16 bit output is allowed");
        }
        this.settings = settings;
        this.module = input;
        this.file = file;
    }

    public void write(double duration) {
        AudioFormat format = new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1,
                true, true);

        int samples = (int) (settings.getSampleRate() * duration);

        byte[] data = getData(module, samples);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        AudioInputStream ais = new AudioInputStream(bais, format,
                samples);

        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getData(Module module, int samples) {

        final int byteRate = settings.getBitRate() / 8;

        // A bit extra
        ByteBuffer output = ByteBuffer.allocate((samples + settings.getBufferSize()) * byteRate);

        ShortBuffer shortBuffer = output.asShortBuffer();
        double scale = FastMath.pow(2, settings.getBitRate() - 1);

        double[] buffer;

        int offset = 0;
        while (offset < samples) {
            buffer = module.getNextSamples();

            for (int i = 0; i < buffer.length; i++) {
                shortBuffer.put((short) (buffer[i] * scale));
            }

            offset += buffer.length;
        }

        byte[] data = new byte[samples * byteRate];
        System.arraycopy(output.array(), 0, data, 0, data.length);
        return data;
    }
}
