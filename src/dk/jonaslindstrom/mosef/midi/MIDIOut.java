package dk.jonaslindstrom.mosef.midi;

import dk.jonaslindstrom.mosef.modules.StoppableModule;
import java.util.function.Predicate;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MIDIOut implements StoppableModule {

  private Receiver receiver;
  private MidiDevice device;

  public MIDIOut(String output) {
    openFirst(info -> info.getName().equals(output));
  }

  /**
   * Open transmission from the first MIDI devices satisfying the given predicate
   */
  private boolean openFirst(Predicate<Info> predicate) {
    for (Info info : MidiSystem.getMidiDeviceInfo()) {
      if (!predicate.test(info)) {
        continue;
      }

      try {
        this.device = MidiSystem.getMidiDevice(info);
        receiver = device.getReceiver();
        device.open();
        return true;
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public void noteOn(int key, double velocity) {
    noteOn(key, velocity, 0);
  }

  public void noteOn(int key, double velocity, int channel) {
    try {
      ShortMessage message = new ShortMessage();
      message.setMessage(ShortMessage.NOTE_ON, channel, key, (int) (velocity * 128));
      receiver.send(message, -1);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  public void noteOff(int key) {
    noteOff(key, 0);
  }

  public void noteOff(int key, int channel) {
    try {
      ShortMessage message = new ShortMessage();
      message.setMessage(ShortMessage.NOTE_OFF, channel, key, 0);
      receiver.send(message, -1);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    device.close();
  }

}