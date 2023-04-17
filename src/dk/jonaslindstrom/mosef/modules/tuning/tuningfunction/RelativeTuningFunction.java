package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction;

public interface RelativeTuningFunction {

    /** Return the ratio between the given note and the base note. Note that <code>getRatio(0) = 1</code> should be true. */
    double getRatio(int steps);

}
