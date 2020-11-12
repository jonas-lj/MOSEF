package dk.jonaslindstrom.mosef.modules.melody;

import dk.jonaslindstrom.mosef.modules.Module;

public class VoiceInputs {

  private final Module pitch, gate, volume;

  public VoiceInputs(Module pitch, Module gate, Module volume) {
    this.pitch = pitch;
    this.gate = gate;
    this.volume = volume;
  }

  public Module getPitch() {
    return pitch;
  }

  public Module getGate() {
    return gate;
  }

  public Module getVolume() {
    return volume;
  }
}
