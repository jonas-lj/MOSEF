package dk.jonaslindstrom.mosef.memory;

/**
 * Instances of this class can store a set of samples (doubles) in a queue data structure with fixed
 * size. If the capacity is reached, the oldest entries are replaced.
 *
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 */
public class SampleMemory {

  private final double[] memory;
  private int pointer;

  /** Create a new SampleMemory object with the given capacity. Initially, all values are set to zero */
  public SampleMemory(int size) {
    this.memory = new double[size];
    this.pointer = 0;
  }

  /** Push a new value to this memory object. If the size of the memory is exceeded, the oldest value */
  public void push(double value) {
    memory[pointer] = value;
    pointer = (pointer + 1) % memory.length;
  }

  /** Get the capacity of this memory object. */
  public int getSize() {
    return memory.length;
  }

  /** Get the <code>n</code>'th newset value. */
  public double get(int n) {
    int i = (pointer - 1 - n + memory.length) % memory.length;
    return memory[i];
  }

}
