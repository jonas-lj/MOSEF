package dk.jonaslindstrom.mosef.modules.patches;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.melody.VoiceBuilder;
import dk.jonaslindstrom.mosef.modules.melody.VoiceInputs;

public class EastCoast implements VoiceBuilder {

  @Override
  public Module build(MOSEF m, VoiceInputs inputs) {
    Module[] pitches = m.split(inputs.getPitch(), 2);
    Module[] gates = m.split(inputs.getGate(), 2);

    Module square =
        m.pureSquare(m.amplifier(pitches[0], 0.5));

    Module saw = m.saw(pitches[1]);

    Module eg1 = m.envelope(gates[0], 0.0, 0.01, 0.0, 0.0);
    Module eg2 = m.envelope(gates[1], 0.01, 0.05, 0.5, 0.2);

    Module lfo = m.sine(0.5);

    Module vcf = m.vcf(m.mixer(square, saw), m.offset(eg1, 20, 200), m.offset(lfo, 0.5, 0.5));
    Module vca = m.amplifier(vcf, eg2);

    return m.amplifier(vca, 0.1);
  }
}
