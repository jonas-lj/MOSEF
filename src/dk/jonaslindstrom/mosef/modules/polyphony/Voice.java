package dk.jonaslindstrom.mosef.modules.polyphony;

import dk.jonaslindstrom.mosef.modules.Module;

public interface Voice {

  void noteOn(int note, double velocity);

  void noteOff(int note);

  boolean ready();

  Module getOutput();

}
