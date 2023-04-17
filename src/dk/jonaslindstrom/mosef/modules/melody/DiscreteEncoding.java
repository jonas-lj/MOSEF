package dk.jonaslindstrom.mosef.modules.melody;

import dk.jonaslindstrom.mosef.util.Pair;
import java.util.ArrayList;

/**
 * Represent a simple encoding of a monophonic track where both note and duration are quantized.
 * This encoding cannot represent breaks and pauses.
 */
public class DiscreteEncoding {

  private final ArrayList<Pair<Integer, Integer>> notes;

  public DiscreteEncoding() {
    this.notes = new ArrayList<>();
  }

  public void addNote(int note, int duration) {
    this.notes.add(Pair.of(note, duration));
  }

  public Track encode(double unitDuration) {
    double t = 0.0;
    Track track = new Track();
    for (Pair<Integer, Integer> note : notes) {
      track.addNote(note.first, t, 0.5, unitDuration * note.second);
      t += unitDuration * note.second;
    }
    return track;
  }

  public static DiscreteEncoding fromTrack(Track track, int bpm, int smallestQuantization) {
    double unit = 4 * 60.0 / (bpm * smallestQuantization);
    return fromTrack(track, unit);
  }

  public static DiscreteEncoding fromTrack(Track track, double unit) {
    DiscreteEncoding encoding = new DiscreteEncoding();

    int i = 0;
    while (i < track.getNotes().size() - 1) {
      Note current = track.getNotes().get(i);
      Note next = track.getNotes().get(i + 1);
      double dt = next.getTime() - current.getTime();
      int duration = Math.toIntExact(Math.round(dt / unit));
      encoding.addNote(current.getNote(), duration);
      i++;
    }

    Note current = track.getNotes().get(i);
    double dt = current.getDuration();
    int duration = Math.toIntExact(Math.round(dt / unit));
    encoding.addNote(current.getNote(), duration);

    return encoding;
  }

  @Override
  public String toString() {
    return "DiscreteEncoding{" +
        "notes=" + notes +
        '}';
  }
}
