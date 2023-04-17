package dk.jonaslindstrom.mosef.modules.polyphony;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.melody.VoiceBuilder;
import dk.jonaslindstrom.mosef.modules.melody.VoiceInputs;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;

public class MonophonicVoice implements Voice {

  private final DynamicModule pitch;
  private final DynamicModule gate;
  private final DynamicModule velocity;
  private final TuningFunction tuning;
  private final Module output;
  private boolean ready;
  private int note;

  public MonophonicVoice(MOSEF m, TuningFunction tuning, VoiceBuilder builder) {
    this.pitch = new DynamicModule(m.getSettings());
    this.gate = new DynamicModule(m.getSettings());
    this.velocity = new DynamicModule(m.getSettings());
    this.tuning = tuning;
    this.output = builder.build(m, new VoiceInputs(pitch, gate, velocity));
    this.ready = true;
  }

  @Override
  public void noteOn(int note, double velocity) {
    this.note = note;
    this.pitch.setOutput(tuning.getFrequency(note));
    this.gate.setOutput(1.0);
    this.velocity.setOutput(1.0);
    this.ready = false;
  }

  @Override
  public void noteOff(int note) {
    this.velocity.setOutput(0.0);
    this.gate.setOutput(0.0);
    this.ready = true;
  }

  @Override
  public boolean ready() {
    return ready;
  }

  public int getCurrentNote() {
    return this.note;
  }

  @Override
  public Module getOutput() {
    return output;
  }
}
