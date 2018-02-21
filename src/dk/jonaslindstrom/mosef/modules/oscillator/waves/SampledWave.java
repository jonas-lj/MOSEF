package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import java.awt.geom.Point2D;
import java.util.function.Function;

import dk.jonaslindstrom.mosef.MOSEFSettings;

public class SampledWave extends SimpleWave {
	
	private Point2D.Float[] samplePoints;
	private float[] slopes;
	
	public SampledWave(MOSEFSettings settings, Function<Float, Float> function, int samples) {
		super(settings);
		cacheSamplePoints(function, samples);
		cacheSlopes();
	}
	
	private void cacheSamplePoints(Function<Float, Float> function, int samples) {
		this.samplePoints = new Point2D.Float[samples];
		for (int i = 0; i < samples; i++) {
			float x = 1.0f * i / (samples - 1);
			this.samplePoints[i] = new Point2D.Float(x, function.apply(x));
		}
	}

	private void cacheSlopes() {
		this.slopes = new float[samplePoints.length-1];
		for (int i = 0; i < samplePoints.length - 1; i++) {
			float dy = (float) (samplePoints[i+1].getY() - samplePoints[i].getY());
			float dx = (float) (samplePoints[i+1].getX() - samplePoints[i].getX());
			slopes[i] =  (dy / dx);
		}		
	}
	
	@Override
	public float getSample(float t) {
		return interpolate(t);
	}

	private float interpolate(double x) {
		int i;
		for (i = 0; i < samplePoints.length - 1; i++) {
			if (samplePoints[i].getX() > x) {
				break;
			}
		}
		i--;
		return (float) (samplePoints[i].getY() + (x - samplePoints[i].getX()) * slopes[i]);
	}
	
}
