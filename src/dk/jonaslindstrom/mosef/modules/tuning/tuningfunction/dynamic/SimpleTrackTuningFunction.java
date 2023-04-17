package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;

import java.util.stream.IntStream;

public class SimpleTrackTuningFunction implements TrackTuningFunction {

    private final TuningFunction tuningFunction;

    public SimpleTrackTuningFunction(TuningFunction tuningFunction) {
        this.tuningFunction = tuningFunction;
    }

    @Override
    public double[] getFrequencies(int[] notes) {
        return IntStream.of(notes).mapToDouble(tuningFunction::getFrequency).toArray();
    }

}
