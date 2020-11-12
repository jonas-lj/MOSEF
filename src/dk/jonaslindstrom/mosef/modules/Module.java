package dk.jonaslindstrom.mosef.modules;

public interface Module {

  /**
   * Iterate the state of the module and return the output sound buffer. Module calling this method
   * on other modules should not change the given buffer.
   */
  double[] getNextSamples();

}
