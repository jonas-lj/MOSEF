package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.Map;

public class Envelope extends SimpleModule {

  private enum EnvelopeStatus {
    ON, OFF, RELEASE
  };

  private double t = 0.0f;
  private double dt;
  private EnvelopeStatus status = EnvelopeStatus.OFF;

  /**
   * 
   * /\ D A / \ S / \_______ / \ R / \
   * 
   * @param attack
   * @param decay
   * @param sustainLevel
   * @param release
   * @param samplesPerSecond
   */
  public Envelope(MOSEFSettings settings, Module attack, Module decay,
      Module sustainLevel, Module release, Module gate) {
    super(settings, Map.of("Attack", attack, "Decay", decay, "Sustain", sustainLevel, "Release",
        release, "Gate", gate));
    this.dt = 1.0f / settings.getSampleRate();
  }

  @Override
  public double getNextSample(double... inputs) {

    double a = inputs[0];
    double d = inputs[1];
    double s = inputs[2];
    double r = inputs[3];
    double g = inputs[4];

    switch (this.status) {
      case OFF:
      default:
        if (g > 0.0f) {
          t = 0.0f;
          this.status = EnvelopeStatus.ON;
        }
        return 0.0f;

      case ON:
        if (g == 0.0f) {
          this.status = EnvelopeStatus.RELEASE;
        }

        t += dt;
        if (t < a) {
          // Attack
          return t / a;
        } else if (t < a + d) {
          // Decay
          return 1.0f - linearInterpolate(d, 1.0f - s, t - a);
        } else {
          // Sustain
          return s;
        }

      case RELEASE:
        if (g > 0.0f) {
          this.status = EnvelopeStatus.ON;
        }

        t += dt;
        if (t < r) {
          return s - linearInterpolate(r, s, t);
        } else {
          this.status = EnvelopeStatus.OFF;
          return 0.0f;
        }

    }
  }

  private double linearInterpolate(double dx, double dy, double x) {
    return x * dy / dx;
  }

}
