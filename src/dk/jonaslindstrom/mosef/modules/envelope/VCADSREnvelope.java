package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.misc.Constant;

import java.util.Map;

public class VCADSREnvelope extends SimpleModule {

  private static final double THRESHOLD = 0.1;
  private static final double MAX = 0.999;
  private static final double MIN = Double.MIN_VALUE;

  private enum EnvelopeStatus {
    ATTACK, DECAY, SUSTAIN, RELEASE, OFF;
  };
  private EnvelopeStatus status = EnvelopeStatus.OFF;

  private enum TriggerStatus {
    ON, OFF;
  };
  private TriggerStatus trigger = TriggerStatus.OFF;

  private double value = 0.0;

  public VCADSREnvelope(MOSEFSettings settings, Module attack, Module decay,
                        Module sustainLevel, Module release, Module gate) {
    super(settings,
             attack,
            decay,
            sustainLevel,
            release,
            gate);
  }

  @Override
  public double getNextSample(double... inputs) {

    double a = inputs[0];
    double d = inputs[1];
    double s = inputs[2];
    double r = inputs[3];
    double g = inputs[4];

    double da = (1.0 / Math.max(a, MIN)) / settings.getSampleRate();
    double dd = (1.0 / Math.max(d, MIN)) / settings.getSampleRate();
    double dr = (1.0 / Math.max(r, MIN)) / settings.getSampleRate();

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
        value += da;
        if (value >= MAX) {
          value = MAX;
          status = EnvelopeStatus.DECAY;
        }
        break;

      case DECAY:
        value -= dd;
        if (value < s) {
          value = s;
          status = EnvelopeStatus.SUSTAIN;
        }
        break;

      case SUSTAIN:
        value = s;
        break;

      case RELEASE:
        value -= dr;
        if (value < 0.0) {
          value = 0.0;
          status = EnvelopeStatus.OFF;
        }
        break;
    }

    return value;
  }

}
