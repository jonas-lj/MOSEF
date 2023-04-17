package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;

public class SimpleArrangementTuningFunction implements ArrangementTuningFunction {

    private final TuningFunction tuningFunction;

    public SimpleArrangementTuningFunction(TuningFunction tuningFunction) {
        this.tuningFunction = tuningFunction;
    }

    @Override
    public double[][] getFrequencies(int[][] notes) {
        int tracks = notes.length;
        int length = notes[0].length;

        double[][] result = new double[tracks][length];
        for (int track = 0; track < tracks; track++) {
            for (int i = 0; i < length; i++) {
                result[track][i] = tuningFunction.getFrequency(notes[track][i]);
            }
        }
        return result;
    }
}
