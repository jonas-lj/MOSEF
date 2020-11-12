package dk.jonaslindstrom.mosef.modules.melody.voices;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.envelope.ADContourEnvelope;
import dk.jonaslindstrom.mosef.modules.melody.VoiceBuilder;
import dk.jonaslindstrom.mosef.modules.melody.VoiceInputs;

public class ElectricPiano implements VoiceBuilder {

  @Override
  public Module build(MOSEF m, VoiceInputs inputs) {
    Module[] f = m.split(inputs.getPitch(), 2);

    Module operator =
        m.sine(10);

    Module osc =
        m.sine(m.offset(operator, f[0], 1));

//    Module envelope = new ADContourEnvelope(m.getSettings(), inputs.getGate(), 0.01, 0.5);

    return m.amplifier(osc, 0.5);
  }
}
