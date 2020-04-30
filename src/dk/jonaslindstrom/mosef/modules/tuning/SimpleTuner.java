package dk.jonaslindstrom.mosef.modules.tuning;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.WellTemperedTuningFunction;
import java.util.Map;

public class SimpleTuner extends SimpleModule implements Tuner {

  private TuningFunction tuningFunction;
  private double frequency;
  private int note;

  public SimpleTuner(MOSEFSettings settings, TuningFunction tuningFunction) {
    super(settings);
    this.tuningFunction = tuningFunction;
    setNote(0);
  }

  public SimpleTuner(MOSEFSettings settings) {
    this(settings, new WellTemperedTuningFunction());
  }

  @Override
  public void setNote(int note) {
    this.note = note;
    this.frequency = tuningFunction.getFrequency(note);
  }

  @Override
  public int getNote() {
    return note;
  }

  @Override
  public double getNextSample(double... inputs) {
    return this.frequency;
  }

}
