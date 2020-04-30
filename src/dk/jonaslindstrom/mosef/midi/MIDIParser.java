package dk.jonaslindstrom.mosef.midi;

import dk.jonaslindstrom.mosef.modules.melody.SimpleMelody;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MIDIParser {

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static SimpleMelody parse(String file) throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(new File(file));

        Map<Integer, Double> activeNotes = new HashMap<>();

        double secondsPerTick = 1.0 / 800.0;

        SimpleMelody melody = new SimpleMelody();

        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON) {
                        System.out.print("@" + event.getTick() + " ");
                        System.out.print("Channel: " + sm.getChannel() + " ");
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();

                        if (velocity == 0) {
                            // Note off -- Reaper registers note-off events as note on with velocity 0
                            assert(activeNotes.containsKey(key));
                            double time = secondsPerTick * activeNotes.get(key);
                            double duration = secondsPerTick * (event.getTick() - activeNotes.get(key));
                            if (duration > 0.0) {
                                melody.addNote(key, time, 1.0, duration);
                            }
                            activeNotes.remove(key);
                            continue;
                        }

                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);

                        activeNotes.put(key, (double) event.getTick());
                    } else if (sm.getCommand() == NOTE_OFF) {
                        System.out.print("@" + event.getTick() + " ");
                        System.out.print("Channel: " + sm.getChannel() + " ");
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);

                        assert(activeNotes.containsKey(key));
                        double time = secondsPerTick * activeNotes.get(key);
                        double duration = secondsPerTick * (event.getTick() - activeNotes.get(key));
                        if (duration > 0.0) {
                            melody.addNote(key, time, 1.0, duration);
                        }
                        activeNotes.remove(key);

                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else if (message instanceof MetaMessage) {
//                    MetaMessage mm = (MetaMessage) message;
//                    System.out.println("MetaMessage: " + Hex.getString(message.getMessage()));
//                    if (message.getMessage()[0] == -1) {
//                        if (message.getMessage()[1] == 0x51) {
//                            // TEMPO
//
//                        } else if (message.getMessage()[1] == 0x58) {
//                            // TICKS PER BEAT
//                            int ticks = message.getMessage()[5];
//                            System.out.println("Ticks per quarter note: " + ticks);
//                        }
//                    }
                } else {
//                    System.out.println("Other message: " + message.getClass());
                }
            }
        }
        System.out.println(melody);
        return melody;
    }
}
