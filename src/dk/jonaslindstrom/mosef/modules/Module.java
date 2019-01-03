package dk.jonaslindstrom.mosef.modules;

import java.util.Map;

public interface Module {

  /**
   * Iterate the state of the module and return the output sound buffer. Input buffers should be
   * left unchanged by a module.
   * 
   * @return
   */
  public double[] getNextSamples();

  public Map<String, Module> getInputs();

}
