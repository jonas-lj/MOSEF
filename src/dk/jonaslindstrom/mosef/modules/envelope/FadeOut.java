package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class FadeOut extends SimpleModule {

  private final double dt;
  private FaderStatus status;
  private double t;

  public FadeOut(MOSEFSettings settings, Module input, Module length, Module gate) {
    super(settings, input, length, gate);
    this.dt = 1.0f / settings.getSampleRate();
    this.status = FaderStatus.OFF;
  }

  @Override
  public double getNextSample(double[] inputs) {

    double i = inputs[0];
    double l = inputs[1];
    double g = inputs[2];

    switch (this.status) {
      case OFF:
      default:
        if (g > 0.0f) {
          this.t = 0.0f;
          this.status = FaderStatus.ON;
        }
        return i;

      case ON:
        t += dt;
        if (t > l) {
          this.status = FaderStatus.OFF;
        }
        return (t / l) * i;
    }
  }

  private enum FaderStatus {
    ON, OFF
  }

}
