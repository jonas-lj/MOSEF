package dk.jonaslindstrom.mosef.modules;

import java.util.Map;

public interface MOSEFModule {

  /**
   * Iterate the state of the module and return the output sound buffer. Input buffers should be
   * left unchanged by a module.
   * 
   * @return
   */
  public float[] getNextSamples();

  public Map<String, MOSEFModule> getInputs();

}
