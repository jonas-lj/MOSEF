package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative;

import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.RelativeTuningFunction;
import org.apache.commons.math3.util.FastMath;

public class FiveLimitTuning implements RelativeTuningFunction {

    private static double[] relative = new double[]{1.0, 16.0 / 15.0, 9.0 / 8.0, 6.0 / 5.0, 5.0 / 4.0,
            4.0 / 3.0, 7.0 / 5.0, 3.0 / 2.0, 8.0 / 5.0, 5.0 / 3.0, 16.0 / 9.0, 15.0 / 8.0};

    @Override
    public double getRatio(int steps) {
        int octave = FastMath.floorDiv(steps, 12);
        int tone = steps - 12 * octave;
        return FastMath.pow(2.0, octave) * relative[tone];
    }
}
