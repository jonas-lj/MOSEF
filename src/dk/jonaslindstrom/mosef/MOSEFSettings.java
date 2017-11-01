package dk.jonaslindstrom.mosef;

public class MOSEFSettings {

	private int samplerate, buffersize, bitrate;
	
	public MOSEFSettings(int samplerate, int buffersize, int bitrate) {
		this.samplerate = samplerate;
		this.buffersize = buffersize;
		this.bitrate = bitrate;
	}
	
	public int getSampleRate() {
		return this.samplerate;
	}
	
	public int getBufferSize() {
		return this.buffersize;
	}
	
	public int getBitRate() {
		return this.bitrate;
	}
	
}
