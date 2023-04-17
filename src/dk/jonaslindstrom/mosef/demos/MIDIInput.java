package dk.jonaslindstrom.mosef.demos;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.patches.EastCoast;
import dk.jonaslindstrom.mosef.modules.polyphony.PolyphonicVoice;
import dk.jonaslindstrom.mosef.modules.polyphony.Voice;

public class MIDIInput {

  public static void main(String[] arguments) {
    MOSEF m = new MOSEF(new MOSEFSettings(44100, 512, 16));

    Voice voice = new PolyphonicVoice(m, 4, new EastCoast());
    m.midiIn(voice, "Axiom USB In");

    m.audioOut(m.amplifier(voice.getOutput(), 0.5));
    m.start();
  }

}
