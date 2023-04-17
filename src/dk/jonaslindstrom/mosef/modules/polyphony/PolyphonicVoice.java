package dk.jonaslindstrom.mosef.modules.polyphony;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.melody.VoiceBuilder;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.examples.EquallyTemperedTuningFunction;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PolyphonicVoice implements Voice {

  private final ArrayList<MonophonicVoice> voices;
  private final PolyphonicStrategy strategy;
  private final Module output;

  public PolyphonicVoice(MOSEF m, int voices, VoiceBuilder builder) {
    this(m, voices, new EquallyTemperedTuningFunction(), builder, PolyphonicStrategy.IGNORE);
  }

  public PolyphonicVoice(MOSEF m, int voices, TuningFunction tuning, VoiceBuilder builder,
      PolyphonicStrategy strategy) {
    this.voices = new ArrayList<>();
    for (int i = 0; i < voices; i++) {
      this.voices.add(new MonophonicVoice(m, tuning, builder));
    }
    this.strategy = strategy;
    this.output = m
        .mixer(this.voices.stream().map(MonophonicVoice::getOutput).collect(Collectors.toList()));
  }

  @Override
  public void noteOn(int note, double velocity) {
    if (strategy == PolyphonicStrategy.IGNORE) {
      for (MonophonicVoice voice : voices) {
        if (voice.ready()) {
          voice.noteOn(note, velocity);
          return;
        }
      }
    }
  }

  @Override
  public void noteOff(int note) {
    for (MonophonicVoice voice : voices) {
      if (voice.getCurrentNote() == note) {
        voice.noteOff(note);
      }
    }
  }

  @Override
  public boolean ready() {
    if (strategy == PolyphonicStrategy.IGNORE) {
      for (MonophonicVoice voice : voices) {
        if (voice.ready()) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public Module getOutput() {
    return output;
  }
}
