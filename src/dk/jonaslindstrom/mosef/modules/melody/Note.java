package dk.jonaslindstrom.mosef.modules.melody;

public class Note implements Comparable<Note> {

  private final int note;
  private final double time;
  private final double velocity;
  private final double duration;

  public Note(int note, double time, double velocity, double duration) {
    this.note = note;
    this.time = time;
    this.velocity = velocity;
    this.duration = duration;
  }

  public int getNote() {
    return note;
  }

  public double getTime() {
    return time;
  }

  public double getVelocity() {
    return velocity;
  }

  public double getDuration() {
    return duration;
  }

  @Override
  public int compareTo(Note o) {
    return Double.compare(this.time, o.time);
  }

  @Override
  public String toString() {
    return "Note{" +
        "note=" + note +
        ", time=" + time +
        ", velocity=" + velocity +
        ", duration=" + duration +
        '}';
  }
}