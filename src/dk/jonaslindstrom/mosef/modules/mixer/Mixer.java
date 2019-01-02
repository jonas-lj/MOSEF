package dk.jonaslindstrom.mosef.modules.mixer;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import java.util.List;

public class Mixer extends SimpleModule {

  /**
   * Create a new mixer module with the given modules as input.
   * 
   * @param modules
   */
  public Mixer(MOSEFSettings settings, MOSEFModule... modules) {
    super(settings, "In", List.of(modules));
  }

  @Override
  public float getNextSample(float... inputs) {

    float output = 0.0f;
    for (int i = 0; i < inputs.length; i++) {
      output += inputs[i];
    }
    return output;
  }



}
