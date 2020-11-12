package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

public class ADSREnvelope extends SimpleModule {

  private static final double THRESHOLD = 0.1;
  private static final double MAX = 0.999;
  private static final double MIN = Double.MIN_VALUE;
  private final double s, Δa, Δd, Δr;

  private EnvelopeStatus status = EnvelopeStatus.OFF;
  private TriggerStatus trigger = TriggerStatus.OFF;

  private double value = 0.0;

  public ADSREnvelope(MOSEFSettings settings, Module gate, double a, double d, double s, double r) {
    super(settings, gate);
    this.s = s;

    this.Δa = (1.0 / FastMath.max(a, MIN)) / settings.getSampleRate();
    this.Δd = (1.0 / FastMath.max(d, MIN)) / settings.getSampleRate();
    this.Δr = (1.0 / FastMath.max(r, MIN)) / settings.getSampleRate();
  }

  @Override
  public double getNextSample(double[] inputs) {
    double g = inputs[0];

    if (trigger == TriggerStatus.OFF && g > THRESHOLD) {
      trigger = TriggerStatus.ON;
      status = EnvelopeStatus.ATTACK;
    } else if (trigger == TriggerStatus.ON && g < THRESHOLD) {
      trigger = TriggerStatus.OFF;
      status = EnvelopeStatus.RELEASE;
    }

    switch (this.status) {
      case OFF:
        value = 0.0;
        break;

      case ATTACK:
        value += Δa;
        if (value >= MAX) {
          value = MAX;
          status = EnvelopeStatus.DECAY;
        }
        break;

      case DECAY:
        value -= Δd;
        if (value < s) {
          value = s;
          status = EnvelopeStatus.SUSTAIN;
        }
        break;

      case SUSTAIN:
        value = s;
        break;

      case RELEASE:
        value -= Δr;
        if (value < 0.0) {
          value = 0.0;
          status = EnvelopeStatus.OFF;
        }
        break;
    }

    return value;
  }

}
