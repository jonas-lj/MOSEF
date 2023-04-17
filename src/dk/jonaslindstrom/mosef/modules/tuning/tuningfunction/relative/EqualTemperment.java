package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.RelativeTuningFunction;
import org.apache.commons.math3.util.FastMath;

public class EqualTemperment implements RelativeTuningFunction {

    private final double notes;

    public EqualTemperment(double notesPerOctave) {
        this.notes = notesPerOctave;
    }

    public double getRatio(int steps) {
        return FastMath.pow(2.0, steps / notes);
    }
}
