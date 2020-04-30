package dk.jonaslindstrom.mosef.modules.sequencers;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class Expander extends SimpleModule {

    private static final double THRESHOLD = 0.1;

    private ExpanderState state;
    private int t;

    private enum ExpanderState {
        ON, OFF
    }

    public Expander(MOSEFSettings settings, Module clock, Module width) {
        super(settings, clock, width);
        this.t = 0;
        this.state = ExpanderState.OFF;
    }

    @Override
    public double getNextSample(double... inputs) {
        if (inputs[0] > THRESHOLD && state == ExpanderState.OFF) {
            state = ExpanderState.ON;
            t = 0;
        } else if (inputs[0] < THRESHOLD && state == ExpanderState.ON) {
            state = ExpanderState.OFF;
        }

        if (++t < inputs[1] * settings.getSampleRate()) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
