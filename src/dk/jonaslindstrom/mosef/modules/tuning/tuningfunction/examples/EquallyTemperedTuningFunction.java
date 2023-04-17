package dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.examples;


import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.OffsetTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative.EqualTemperment;

public class EquallyTemperedTuningFunction extends OffsetTuningFunction {

  public EquallyTemperedTuningFunction(double centerFrequency, int notesPerOctave, int centerKey) {
    super(new EqualTemperment(notesPerOctave), centerKey, centerFrequency);
  }

  public EquallyTemperedTuningFunction() {
    this(440, 12, 69);
  }

}
