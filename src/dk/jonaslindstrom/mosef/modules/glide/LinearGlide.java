package dk.jonaslindstrom.mosef.modules.glide;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

public class LinearGlide extends SimpleModule {

  private final double delta;
  private double current;

  public LinearGlide(MOSEFSettings settings, Module in, double speed) {
    super(settings, in);
    this.delta = speed / settings.getSampleRate();
    this.current = 0.0;
  }

  @Override
  public double getNextSample(double[] inputs) {
    double goal = inputs[0];
    if (goal > current) {
      current = FastMath.min(goal, current + delta);
    } else if (goal < current) {
      current = FastMath.max(goal, current - delta);
    }
    return current;
  }
}
