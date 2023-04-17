package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public class OffsetTuningFunction implements TuningFunction {

    private final RelativeTuningFunction tuningFunction;
    private final double base;
    private final int offset;

    public OffsetTuningFunction(RelativeTuningFunction tuningFunction, int offset, double base) {
        this.tuningFunction = tuningFunction;
        this.base = base;
        this.offset = offset;
    }

    @Override
    public double getFrequency(int note) {
        return tuningFunction.getRatio(note - offset) * base;
    }
}
