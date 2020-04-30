package dk.jonaslindstrom.mosef.modules.envelope;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.Module;

public class ADSREnvelope extends SimpleModule {

    private static final double THRESHOLD = 0.1;
    private static final double MAX = 0.999;
    private static final double MIN = Double.MIN_VALUE;

    private enum EnvelopeStatus {
        ATTACK, DECAY, SUSTAIN, RELEASE, OFF;
    };
    private ADSREnvelope.EnvelopeStatus status = ADSREnvelope.EnvelopeStatus.OFF;

    private enum TriggerStatus {
        ON, OFF;
    };
    private ADSREnvelope.TriggerStatus trigger = ADSREnvelope.TriggerStatus.OFF;

    private double value = 0.0;
    private final double s, Δa, Δd, Δr;

    public ADSREnvelope(MOSEFSettings settings, Module gate, double a, double d, double s, double r) {
        super(settings, gate);
        this.s = s;

        this.Δa = (1.0 / Math.max(a, MIN)) / settings.getSampleRate();
        this.Δd = (1.0 / Math.max(d, MIN)) / settings.getSampleRate();
        this.Δr = (1.0 / Math.max(r, MIN)) / settings.getSampleRate();
    }

    @Override
    public double getNextSample(double... inputs) {
        double g = inputs[0];

        if (trigger == ADSREnvelope.TriggerStatus.OFF && g > THRESHOLD) {
            trigger = ADSREnvelope.TriggerStatus.ON;
            status = ADSREnvelope.EnvelopeStatus.ATTACK;
        } else if (trigger == ADSREnvelope.TriggerStatus.ON && g < THRESHOLD) {
            trigger = ADSREnvelope.TriggerStatus.OFF;
            status = ADSREnvelope.EnvelopeStatus.RELEASE;
        }

        switch (this.status) {
            case OFF:
                value = 0.0;
                break;

            case ATTACK:
                value += Δa;
                if (value >= MAX) {
                    value = MAX;
                    status = ADSREnvelope.EnvelopeStatus.DECAY;
                }
                break;

            case DECAY:
                value -= Δd;
                if (value < s) {
                    value = s;
                    status = ADSREnvelope.EnvelopeStatus.SUSTAIN;
                }
                break;

            case SUSTAIN:
                value = s;
                break;

            case RELEASE:
                value -= Δr;
                if (value < 0.0) {
                    value = 0.0;
                    status = ADSREnvelope.EnvelopeStatus.OFF;
                }
                break;
        }

        return value;
    }
}
