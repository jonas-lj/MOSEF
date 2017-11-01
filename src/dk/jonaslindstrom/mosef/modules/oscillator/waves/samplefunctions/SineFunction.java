package dk.jonaslindstrom.mosef.modules.oscillator.waves.samplefunctions;

public class SineFunction implements SampleFunction {

	@Override
	public float getValue(float x) {		
		return (float) Math.sin(2 * Math.PI * x);
	}

}
