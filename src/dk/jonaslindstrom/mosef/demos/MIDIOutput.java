package dk.jonaslindstrom.mosef.demos;

import dk.jonaslindstrom.mosef.midi.MIDIOut;
import dk.jonaslindstrom.mosef.modules.scales.Scale;
import dk.jonaslindstrom.mosef.modules.scales.ScaleFactory;
import dk.jonaslindstrom.mosef.modules.scales.ScaleFactory.Key;

public class MIDIOutput {

  public static void main(String[] arguments) throws InterruptedException {

    MIDIOut out = new MIDIOut("Axiom USB Out");

    Thread.sleep(1000);

    Scale scale = ScaleFactory.major(Key.A);
    for (int j = 0; j < 10; j++) {
      for (int i = 0; i < 12; i++) {
        int note = 33 + scale.noteAt(i);
        out.noteOn(note, 0.5);
        Thread.sleep(300);
        out.noteOff(note);
        Thread.sleep(100);
      }

      for (int i = 12; i >= 0; i--) {
        int note = 33 + scale.noteAt(i);
        out.noteOn(note, 0.5);
        Thread.sleep(300);
        out.noteOff(note);
        Thread.sleep(100);
      }
    }


    out.stop();

  }

}
