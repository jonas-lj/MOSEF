package dk.jonaslindstrom.mosef.midi;

import dk.jonaslindstrom.mosef.modules.melody.Track;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;

public class MIDIParser {

  /**
   * Read a track from a MIDI file. All note-events in the file are stored in the track.
   *
   * @param file
   * @param bpm
   * @return
   * @throws InvalidMidiDataException
   * @throws IOException
   */
  public static Track parse(String file, int bpm) throws InvalidMidiDataException, IOException {
    Sequence sequence = MidiSystem.getSequence(new File(file));

    int ticksPerBeat = sequence.getResolution();
    int ticksPerSecond = ticksPerBeat * bpm / 60;
    double secondsPerTick = 1.0 / ticksPerSecond;

    Map<Integer, Double> activeNotes = new HashMap<>();

    Track track = new Track();
    for (javax.sound.midi.Track midiTrack : sequence.getTracks()) {
      for (int i = 0; i < midiTrack.size(); i++) {
        MidiEvent event = midiTrack.get(i);
        MidiMessage message = event.getMessage();
        if (message instanceof ShortMessage) {
          ShortMessage sm = (ShortMessage) message;
          if (sm.getCommand() == ShortMessage.NOTE_ON) {
            int key = sm.getData1();
            int velocity = sm.getData2();

            if (velocity == 0 && activeNotes.containsKey(key)) {
              // Note off -- Reaper registers note-off events as note-on with velocity 0
              double time = secondsPerTick * activeNotes.get(key);
              double duration = secondsPerTick * (event.getTick() - activeNotes.get(key));
              if (duration > 0.0) {
                track.addNote(key, time, 1.0, duration);
              }
              activeNotes.remove(key);
              continue;
            }

            activeNotes.put(key, (double) event.getTick());
          } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
            int key = sm.getData1();

            assert (activeNotes.containsKey(key));
            double time = secondsPerTick * activeNotes.get(key);
            double duration = secondsPerTick * (event.getTick() - activeNotes.get(key));
            if (duration > 0.0) {
              track.addNote(key, time, 1.0, duration);
            }
            activeNotes.remove(key);
          }
        }
      }
    }
    return track;
  }
}
