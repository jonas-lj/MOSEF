package dk.jonaslindstrom.mosef.modules.oscillator.waves;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import java.awt.geom.Point2D;
import java.util.function.DoubleUnaryOperator;

public class SampledWave extends SimpleWave {

  private Point2D.Double[] samplePoints;
  private double[] slopes;

  public SampledWave(MOSEFSettings settings, DoubleUnaryOperator function, int samples) {
    super(settings);
    cacheSamplePoints(function, samples);
    cacheSlopes();
  }

  private void cacheSamplePoints(DoubleUnaryOperator function, int samples) {
    this.samplePoints = new Point2D.Double[samples];
    for (int i = 0; i < samples; i++) {
      double x = 1.0f * i / (samples - 1);
      this.samplePoints[i] = new Point2D.Double(x, function.applyAsDouble(x));
    }
  }

  private void cacheSlopes() {
    this.slopes = new double[samplePoints.length - 1];
    for (int i = 0; i < samplePoints.length - 1; i++) {
      double dy = samplePoints[i + 1].getY() - samplePoints[i].getY();
      double dx = samplePoints[i + 1].getX() - samplePoints[i].getX();
      slopes[i] = (dy / dx);
    }
  }

  @Override
  public double getSample(double t) {
    return interpolate(t);
  }

  private double interpolate(double x) {
    int i;
    for (i = 0; i < samplePoints.length - 1; i++) {
      if (samplePoints[i].getX() > x) {
        break;
      }
    }
    i--;
    return samplePoints[i].getY() + (x - samplePoints[i].getX()) * slopes[i];
  }

}
