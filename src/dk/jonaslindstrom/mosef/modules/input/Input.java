package dk.jonaslindstrom.mosef.modules.input;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleConverter;
import dk.jonaslindstrom.mosef.modules.Module;

public class Input implements Module {

	private MOSEFSettings settings;
	private TargetDataLine targetLine;
	private float[] buffer;	
	private byte[] bytes;
	private int byterate;
	private SampleConverter converter;
	
	/**
	 * Instances of this module provide sound from input sources, eg.
	 * instruments or microphones. This module uses the default sound input
	 * source.
	 * 
	 * @param samplerate
	 *            Number of samples per second, eg. 44100.
	 * @param bitrate
	 *            Bits per sample, eg. 16.
	 * @param buffersize
	 *            Size of the buffer used for this input source. Low buffersize
	 *            may decrease latency but may cause stutter if the buffer runs
	 *            out of data.
	 */
	public Input(MOSEFSettings settings) {
		this.settings = settings;
				
		try {
			// Open the default input source
			boolean bigEndian = true;
			
			AudioFormat format = new AudioFormat(settings.getSampleRate(), settings.getBitRate(), 1, true, bigEndian);
			this.converter = new SampleConverter(settings.getBitRate(), bigEndian);
			DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
			targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
			targetLine.open(format, settings.getBufferSize());
			targetLine.start();
			
			this.byterate = settings.getBitRate() / 8; 			
			this.bytes = new byte[settings.getBufferSize() * byterate];
			this.buffer = new float[settings.getBufferSize()];
				
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	@Override
	public float[] getNextSamples() {
		
		int numBytesRead = targetLine.read(bytes, 0, settings.getBufferSize() * byterate);
		if (numBytesRead == -1) {
			System.out.println("Failed to read from audio input source");
			return null;
		}

		this.converter.fillSamples(buffer, bytes);
		return buffer;
	}

}
