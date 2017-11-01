package dk.jonaslindstrom.mosef.memory;

public class SampleConverter {

	private int byteRate;
	private float scale;
	private boolean bigEndian;

	public SampleConverter(int bitRate, boolean bigEndian) {
		System.out.println(bitRate);
		
		this.byteRate = bitRate / 8;
		this.scale = (float) Math.pow(2, -bitRate + 1);		
		this.bigEndian = bigEndian;
	}
	
	public void fillSamples(float[] samples, byte[] bytes) {

		for (int i = 0; i < samples.length; i++) {
			int s = 0;
			
			if (bigEndian) {
				// TODO: Untested. Does it work for 24 bit?
				for (int j = 0; j < byteRate - 1; j++) {
					s |= ((bytes[byteRate * i + j]) << ((byteRate - j - 1) * 8)) & 0xFF;
				}
				s |= bytes[byteRate * i + byteRate - 1] & 0xff;				
				
			} else {
				for (int j = 1; j < byteRate; j++) {
					s |= (bytes[byteRate * i + j]) << (j * 8);
									}
				s |= bytes[byteRate * i] & 0xff;				
			}
			
			samples[i] = s * scale;
		}
		
	}
	
	public void fillBytes(byte[] bytes, float[] samples) {
		
	}
	
}
