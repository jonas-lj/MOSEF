package dk.jonaslindstrom.mosef.modules.tuning;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.WellTemperedTuningFunction;
import java.util.Map;

public class SimpleTuner extends SimpleModule implements Tuner {

  private TuningFunction tuningFunction;
  private float frequency;
  private int note;

  public SimpleTuner(MOSEFSettings settings, TuningFunction tuningFunction) {
    super(settings, Map.of());
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
  public float getNextSample(float... inputs) {
    return this.frequency;
  }

}
