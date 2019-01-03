package dk.jonaslindstrom.mosef.modules.tuning.interpolate;

public class QuadraticInterpolateFunction implements InterpolateFunction {

  @Override
  public double interpolate(double t) {
    return t * t;
  }

}
