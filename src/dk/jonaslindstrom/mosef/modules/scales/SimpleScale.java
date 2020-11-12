package dk.jonaslindstrom.mosef.modules.scales;

import java.util.Arrays;

public class SimpleScale implements Scale {

  private final int base;
  private final int[] scale;
  private final String name;

  SimpleScale(int root, int[] scale) {
    this(root, scale, NoteName.noteAt(root) + "(" + Arrays.toString(scale) + ")");
  }

  SimpleScale(int base, int[] scale, String name) {
    this.base = base;
    this.scale = scale;
    this.name = name;
  }

  @Override
  public int noteAt(int position) {
    int octave = Math.floorDiv(position, scale.length);
    int note = Math.floorMod(position, scale.length);
    return base + octave * 12 + scale[note];
  }

  @Override
  public String toString() {
    return name;
  }
}
