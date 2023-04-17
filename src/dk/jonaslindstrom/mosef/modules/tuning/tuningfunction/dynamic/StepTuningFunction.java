package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.RelativeTuningFunction;

public class StepTuningFunction implements TrackTuningFunction {

    private final RelativeTuningFunction tuningFunction;
    private final double base;

    public StepTuningFunction(double base, RelativeTuningFunction tuningFunction) {
        this.base = base;
        this.tuningFunction = tuningFunction;
    }

    @Override
    public double[] getFrequencies(int[] notes) {
        double[] frequencies = new double[notes.length];
        frequencies[0] = base;
        for (int i = 1; i < notes.length; i++) {
            frequencies[i] = frequencies[i -1] * tuningFunction.getRatio(notes[i] - notes[i-1]);
        }
        return frequencies;
    }
}
