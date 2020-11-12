package dk.jonaslindstrom.mosef.modules.glide;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

public class ExponentialGlide extends SimpleModule {

  private final double speed;
  private final double speedInverse;
  private double current;

  public ExponentialGlide(MOSEFSettings settings, Module in, double speed) {
    super(settings, in);
    this.speed = speed;
    this.speedInverse = 1.0 / speed;
    this.current = 0.0;
  }

  @Override
  public double getNextSample(double[] inputs) {
    double goal = inputs[0];
    if (goal > current) {
      current = FastMath.min(goal, current * speed);
    } else if (goal < current) {
      current = FastMath.max(goal, current * speedInverse);
    }
    return current;
  }
}
