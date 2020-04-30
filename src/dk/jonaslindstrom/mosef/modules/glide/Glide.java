package dk.jonaslindstrom.mosef.modules.glide;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class Glide extends SimpleModule {

  private final double delta;
  private double current;

  public Glide(MOSEFSettings settings, Module in, double speed) {
    super(settings, in);
    this.delta = speed / settings.getSampleRate();
    this.current = 0.0;
  }

  @Override
  public double getNextSample(double... inputs) {
    double goal = inputs[0];
    if (goal > current) {
      current = Math.min(goal, current + delta);
    } else if (goal < current) {
      current = Math.max(goal, current - delta);
    }

    return current;
  }
}
