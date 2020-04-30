package dk.jonaslindstrom.mosef.modules.melody;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.TuningFunction;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.*;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.stream.Collectors;

public class SimpleMelody {

    private final ArrayList<Note> notes;

    public SimpleMelody() {
        this.notes = new ArrayList<Note>();
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

    public Pair<Module, Module> getMonophonicVoice(MOSEFSettings settings, TuningFunction tuning) {
        return Pair.of(new MonophonicFrequencyModule(settings, tuning, this),
                new MonophonicGateModule(settings, this));
    }

    public List<Pair<Module, Module>> getPolyphonicVoices(MOSEFSettings settings, TuningFunction tuning, int voices) {
        List<SimpleMelody> melodies = new ArrayList<>();

        // When is this voice available again
        double[] available = new double[voices];

        for (int i = 0; i < voices; i++) {
            melodies.add(new SimpleMelody());
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

        return melodies.stream().map(m -> m.getMonophonicVoice(settings, tuning)).collect(Collectors.toList());
    }

    private class MonophonicFrequencyModule extends SimpleModule {

        private final TuningFunction tuning;
        private final List<Note> melody;
        private final double δ;

        private double t;
        private int i;
        private double out;

        public MonophonicFrequencyModule(MOSEFSettings settings, TuningFunction tuning, SimpleMelody melody) {
            super(settings);
            this.t = 0;
            this.δ = 1.0 / settings.getSampleRate();
            this.i = 0;
            this.tuning = tuning;
            this.melody = melody.getNotes();
        }

        @Override
        public double getNextSample(double... inputs) {
            t += δ;

            if (i < melody.size() && t > melody.get(i).getTime()) {
                this.out = tuning.getFrequency(melody.get(i).getNote());
                i++;
            }

            return out;
        }
    }

    private class MonophonicGateModule extends SimpleModule {

        private double t;
        private final double δ;
        private int i;
        private final List<Note> melody;
        private boolean active;

        public MonophonicGateModule(MOSEFSettings settings, SimpleMelody melody) {
            super(settings);
            this.t = 0;
            this.δ = 1.0 / settings.getSampleRate();
            this.i = 0;
            this.melody = melody.getNotes();
        }

        @Override
        public double getNextSample(double... inputs) {
            t += δ;

            if (i < melody.size() && t > melody.get(i).getTime()) {
                this.active = true;
                i++;
                return 0.0;
            }

            if (i > 1 && active && t >= melody.get(i-1).getTime() + melody.get(i-1).getDuration()) {
                this.active = false;
            }

            return active ? 1.0 : 0.0;
        }
    }

    public String toString() {
        return notes.toString();
    }

}