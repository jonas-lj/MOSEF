package dk.jonaslindstrom.mosef.modules.melody;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Track extends AbstractCollection<Note> implements Serializable, Collection<Note> {

  private final ArrayList<Note> notes;

  public Track() {
    this.notes = new ArrayList<Note>();
  }

  @Override
  public Iterator<Note> iterator() {
    return notes.iterator();
  }

  @Override
  public int size() {
    return notes.size();
  }

  public static Track decode(String encoded) throws IOException, ClassNotFoundException {
    Encoder encoder = new DefaultEncoder();
    return encoder.decode(encoded);
  }

  public void addNote(Note note) {
    notes.add(note);
    Collections.sort(notes);
  }

  public void addNote(int note, double time, double velocity, double duration) {
    notes.add(new Note(note, time, velocity, duration));
  }

  public List<Note> getNotes() {
    return Collections.unmodifiableList(notes);
  }

  public VoiceInputs getMonophonicVoice(MOSEFSettings settings, TuningFunction tuning) {
    return new VoiceInputs(new MonophonicPitchModule(settings, tuning, this),
        new MonophonicGateModule(settings, this), new MonophonicVelocityModule(settings, this));
  }

  public List<VoiceInputs> getPolyphonicVoices(MOSEFSettings settings,
      TuningFunction tuning, int voices) {
    List<Track> melodies = new ArrayList<>();

    // When is this voice available again
    double[] available = new double[voices];

    for (int i = 0; i < voices; i++) {
      melodies.add(new Track());
    }

    for (Note note : notes) {
      for (int i = 0; i < voices; i++) {
        if (note.getTime() >= available[i]) {
          melodies.get(i).addNote(note);
          available[i] = note.getTime() + note.getDuration();
          break;
        }
      }
    }

    return melodies.stream().map(m -> m.getMonophonicVoice(settings, tuning))
        .collect(Collectors.toList());
  }

  public String toString() {
    return notes.toString();
  }

  public String encode() throws IOException {
    Encoder encoder = new DefaultEncoder();
    return encoder.encode(this);
  }

  private static class MonophonicPitchModule extends SimpleModule {

    private final TuningFunction tuning;
    private final List<Note> melody;
    private final double δ;

    private double t;
    private int i;
    private double out;

    public MonophonicPitchModule(MOSEFSettings settings, TuningFunction tuning, Track melody) {
      super(settings);
      this.t = 0;
      this.δ = 1.0 / settings.getSampleRate();
      this.i = 0;
      this.tuning = tuning;
      this.melody = melody.getNotes();
    }

    @Override
    public double getNextSample(double[] inputs) {
      t = t + δ;

      if (i < melody.size() && t > melody.get(i).getTime()) {
        this.out = tuning.getFrequency(melody.get(i).getNote());
        i++;
      }

      return out;
    }
  }

  private static class MonophonicGateModule extends SimpleModule {

    private final double δ;
    private final List<Note> melody;
    private double t;
    private int i;
    private boolean active;

    public MonophonicGateModule(MOSEFSettings settings, Track melody) {
      super(settings);
      this.t = 0;
      this.δ = 1.0 / settings.getSampleRate();
      this.i = 0;
      this.melody = melody.getNotes();
    }

    @Override
    public double getNextSample(double[] inputs) {
      t = t + δ;

      if (i < melody.size() && t > melody.get(i).getTime()) {
        this.active = true;
        i++;
        return 0.0;
      }

      if (i > 0 && active && t >= melody.get(i - 1).getTime() + melody.get(i - 1).getDuration()) {
        this.active = false;
      }

      return active ? 1.0 : 0.0;
    }
  }

  private static class MonophonicVelocityModule extends SimpleModule {

    private final List<Note> melody;
    private final double δ;

    private double t;
    private int i;
    private double out;

    public MonophonicVelocityModule(MOSEFSettings settings, Track melody) {
      super(settings);
      this.t = 0;
      this.δ = 1.0 / settings.getSampleRate();
      this.i = 0;
      this.melody = melody.getNotes();
    }

    @Override
    public double getNextSample(double[] inputs) {
      t = t + δ;

      if (i < melody.size() && t > melody.get(i).getTime()) {
        this.out = melody.get(i).getVelocity();
        i++;
      }

      return out;
    }
  }
}