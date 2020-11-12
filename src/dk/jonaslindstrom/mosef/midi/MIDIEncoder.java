package dk.jonaslindstrom.mosef.midi;

import dk.jonaslindstrom.mosef.modules.melody.Note;
import dk.jonaslindstrom.mosef.modules.melody.Track;
import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public class MIDIEncoder {

  /**
   * Save the given track as a one-track MIDI file.
   *
   * @param track The track to encode.
   * @param file The path where the MIDI file is saved.
   * @throws InvalidMidiDataException
   * @throws IOException
   */
  public static void encode(Track track, String file) throws InvalidMidiDataException, IOException {

    // 48 ticks per second for 120 bpm -- 24 per beat
    Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ, 24);
    javax.sound.midi.Track t = s.createTrack();

    // turn on General MIDI sound set  ****
    byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
    SysexMessage sm = new SysexMessage();
    sm.setMessage(b, 6);
    MidiEvent me = new MidiEvent(sm, 0);
    t.add(me);

    // set tempo (meta event) to 120 bpm
    MetaMessage mt = new MetaMessage();
    byte[] bt = {0x07, (byte) 0xa1, 0x20};
    mt.setMessage(0x51, bt, 3);
    me = new MidiEvent(mt, 0);
    t.add(me);

    ShortMessage mm = new ShortMessage();

    // set poly on
    mm.setMessage(0xB0, 0x7F, 0x00);
    me = new MidiEvent(mm, 0);
    t.add(me);

    // Add note on and note offs for all notes in track
    for (Note note : track.getNotes()) {
      mm = new ShortMessage();
      mm.setMessage(ShortMessage.NOTE_ON, note.getNote(), 0x60);
      me = new MidiEvent(mm, (long) (note.getTime() * 48.0));
      t.add(me);

      mm = new ShortMessage();
      mm.setMessage(ShortMessage.NOTE_OFF, note.getNote(), 0x00);
      me = new MidiEvent(mm, (long) ((note.getTime() + note.getDuration()) * 48.0));
      t.add(me);
    }

    // End song after last note finished
    long end = (long) (48 * track.getNotes().stream()
        .mapToDouble(note -> note.getTime() + note.getDuration()).max().getAsDouble());
    mt = new MetaMessage();
    byte[] bet = {}; // empty array
    mt.setMessage(0x2F, bet, 0);
    me = new MidiEvent(mt, end);
    t.add(me);

    File f = new File(file);
    MidiSystem.write(s, 1, f);
  }

}
