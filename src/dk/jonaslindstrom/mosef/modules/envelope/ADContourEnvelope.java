package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import org.apache.commons.math3.util.FastMath;

public class ADContourEnvelope extends SimpleModule {

  private static final double THRESHOLD = 0.1;
  private static final double MAX = 0.999;
  private static final double MIN = Double.MIN_VALUE;
  private final double Δa, Δd;

  private EnvelopeStatus status = EnvelopeStatus.OFF;
  private TriggerStatus trigger = TriggerStatus.OFF;

  private double value = 0.0;

  public ADContourEnvelope(MOSEFSettings settings, Module gate, double a, double d) {
    super(settings, gate);
    this.Δa = Math.exp(Math.log(2) / (FastMath.max(a, MIN) * settings.getSampleRate()));
    this.Δd = Math.exp(-Math.log(2) / (FastMath.max(d, MIN) * settings.getSampleRate()));
  }

  @Override
  public double getNextSample(double[] inputs) {
    double g = inputs[0];

    if (trigger == TriggerStatus.OFF && g > THRESHOLD) {
      trigger = TriggerStatus.ON;
      status = EnvelopeStatus.ATTACK;
    } else if (trigger == TriggerStatus.ON && g < THRESHOLD) {
      trigger = TriggerStatus.OFF;
      status = EnvelopeStatus.OFF;
    }

    switch (this.status) {
      case OFF:
        value = 0.0;
        break;

      case ATTACK:
        value = (value + 1) * Δa - 1;
        if (value >= MAX) {
          value = MAX;
          status = EnvelopeStatus.DECAY;
        }
        break;

      case DECAY:
        value = (value + 1) * Δd - 1;
        if (value < 0) {
          value = 0;
          status = EnvelopeStatus.OFF;
        }
        break;
    }

    return value;
  }

}
