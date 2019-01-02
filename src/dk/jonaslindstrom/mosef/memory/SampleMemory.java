package dk.jonaslindstrom.mosef.memory;

/**
 * Instances of this class can store a set of samples (floats) in a queue data structure with fixed
 * size. If the capacity is reached, the oldest entries are replaced.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class SampleMemory {

  private float[] memory;
  private int pointer;

  public SampleMemory(int size) {
    this.memory = new float[size];
    this.pointer = 0;
  }

  /**
   * Push a new value to this memory object. If the size of the memory is exceeded, the oldest value
   * is removed.
   * 
   * @param value
   */
  public void push(float value) {
    memory[pointer] = value;
    pointer = (pointer + 1) % memory.length;
  }

  /**
   * Get the capacity of this memory object.
   * 
   * @return
   */
  public int getSize() {
    return memory.length;
  }

  /**
   * Get the <code>n</code>'th newset value.
   * 
   * @param n
   * @return
   */
  public float get(int n) {
    int i = (pointer - n + memory.length) % memory.length;
    return memory[i];
  }

}
