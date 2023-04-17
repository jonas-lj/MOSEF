package dk.jonaslindstrom.mosef.midi;

import dk.jonaslindstrom.mosef.modules.StoppableModule;
import dk.jonaslindstrom.mosef.modules.polyphony.Voice;
import java.io.Closeable;
import java.util.function.Predicate;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class MIDIIn implements StoppableModule, Receiver {

  private final Voice voice;

  public MIDIIn(Voice voice, String input) {
    this.voice = voice;
    boolean found = openAll(info -> info.getName().equals(input));
    assert(found);
  }

  public MIDIIn(Voice voice) {
    this.voice = voice;
    openAll(info -> true);
  }

  /** Open transmission from all MIDI devices satisfying the given predicate */
  private boolean openAll(Predicate<Info> predicate) {
    boolean found = false;
    for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
      if (!predicate.test(info)) {
        continue;
      }

      try {
        MidiDevice device = MidiSystem.getMidiDevice(info);
        Transmitter transmitter = device.getTransmitter();
        transmitter.setReceiver(this);
        device.open();
        found = true;
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
      }

    }
    return found;
  }

  private void noteOn(int key, double velocity) {
    voice.noteOn(key, velocity);
  }

  private void noteOff(int key) {
    voice.noteOff(key);
  }

  @Override
  public void send(MidiMessage message, long timeStamp) {
    System.out.println("MIDI received: " + message + ", " + timeStamp);
    if (message instanceof ShortMessage) {
      ShortMessage shortMessage = (ShortMessage) message;
      if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
        int key = shortMessage.getData1();
        int velocity = shortMessage.getData2();
        if (velocity > 0) {
          noteOn(key, 127.0 / velocity);
        } else {
          noteOff(key);
        }
      } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
        int key = shortMessage.getData1();
        noteOff(key);
      }
    }
  }

  @Override
  public void close() {
    for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
      try {
        MidiDevice device = MidiSystem.getMidiDevice(info);
        device.close();
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void stop() {
    close();
  }
}