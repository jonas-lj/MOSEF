package dk.jonaslindstrom.mosef.modules.sample;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

public class Sample implements Module {

	private MOSEFSettings settings;
	private FloatBuffer sampleBuffer;
	private float[] buffer;
	private boolean wrap;

	public Sample(MOSEFSettings settings, float[] sample, boolean wrap) {
		this.settings = settings;
		this.sampleBuffer = FloatBuffer.wrap(sample);
		this.buffer = new float[settings.getBufferSize()];
		this.wrap = wrap;
	}

	public Sample(MOSEFSettings settings, float[] sample) {
		this(settings, sample, false);
	}
	
	@Override
	public float[] getNextSamples() {
		
		if (!wrap) {
			// Sample play once
			if (sampleBuffer.remaining() > buffer.length) {
				sampleBuffer.get(buffer);
			} else {
				sampleBuffer.get(buffer, 0, sampleBuffer.remaining());
				Arrays.fill(buffer, sampleBuffer.remaining(), buffer.length, 0.0f);
			}
		} else {
			
			// Wraps around
			int index = 0;
			do {
				int inc = Math.min(buffer.length - index, sampleBuffer.remaining());
				sampleBuffer.get(buffer, index, inc);
				index += inc;
				if (!sampleBuffer.hasRemaining()) {
					sampleBuffer.rewind();
				}
			} while (index < buffer.length);
				
		}		
		return buffer;
	}
	
	public void save(File file) throws UnsupportedAudioFileException, IOException {
		float scale = (float) Math.pow(2, settings.getBitRate() - 1);
		int byteRate = settings.getBitRate() / 8;
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(sampleBuffer.capacity() * byteRate);
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
		
		sampleBuffer.rewind();
		while (sampleBuffer.hasRemaining()) {
			shortBuffer.put((short) (sampleBuffer.get() * scale));
		}

		if (!byteBuffer.hasArray()) {
			throw new UnsupportedOperationException("ByteBuffer does not have array");
		}
		byte[] bytes = byteBuffer.array();
		
		AudioFormat format = new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1, true, true);
		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(bytes), format, bytes.length);
	
		AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		
	}

}
