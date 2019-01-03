package dk.jonaslindstrom.mosef.modules.filter.filters.windows;

public class BartlettHannWindow implements Window {

  private int length;

  public BartlettHannWindow(int length) {
    this.length = length;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public double getCoefficient(int k) {
    // No need to use wavetable - windows are just called once when creating
    // a filter.
    return (double) (0.62f - 0.48f * Math.abs(k / (this.length - 1) - 0.5f)
        - 0.38f * Math.cos(2.0f * Math.PI * k / (this.length - 1)));
  }

}
