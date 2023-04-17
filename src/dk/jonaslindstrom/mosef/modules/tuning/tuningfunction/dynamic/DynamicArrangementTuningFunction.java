package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.RelativeTuningFunction;

public class DynamicArrangementTuningFunction implements ArrangementTuningFunction {

    private final RelativeTuningFunction harmony;
    private final TrackTuningFunction lead;

    public DynamicArrangementTuningFunction(TrackTuningFunction lead, RelativeTuningFunction harmony) {
        this.lead = lead;
        this.harmony = harmony;
    }

    @Override
    public double[][] getFrequencies(int[][] notes) {
        double[][] frequencies = new double[notes.length][];
        frequencies[0] = lead.getFrequencies(notes[0]);

        for (int tracks = 1; tracks < notes.length; tracks++) {
            frequencies[tracks] = new double[notes[tracks].length];
            for (int i = 0; i < notes[tracks].length; i++) {
                frequencies[tracks][i] = frequencies[0][i] * harmony.getRatio(notes[tracks][i] - notes[0][i]);
            }
        }
        return frequencies;
    }
}
