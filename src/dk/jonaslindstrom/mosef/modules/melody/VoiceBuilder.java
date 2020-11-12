package dk.jonaslindstrom.mosef.modules.melody;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

@FunctionalInterface
public interface VoiceBuilder {

  Module build(MOSEF m, VoiceInputs inputs);

}
