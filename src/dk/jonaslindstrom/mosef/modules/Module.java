package dk.jonaslindstrom.mosef.modules;

import dk.jonaslindstrom.mosef.modules.splitter.Splitter;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.List;

public interface Module {

  /**
   * Iterate the state of the module and return the output sound buffer. Input buffers should be
   * left unchanged by a module.
   * 
   * @return
   */
  double[] getNextSamples();

}
