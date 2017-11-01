package dk.jonaslindstrom.mosef.modules.filter.filters.windows;

public class HammingWindow implements Window {

	private int length;

	public HammingWindow(int length) {
		this.length = length;
	}
	
	@Override
	public int getLength() {
		return this.length;
	}

	@Override
	public float getCoefficient(int k) {
		// No need to use wavetable - windows are just called once when creating a filter.
		return (float) (0.54f - 0.46f * Math.cos(2.0f * Math.PI * k / (this.length - 1)));
	}

}
