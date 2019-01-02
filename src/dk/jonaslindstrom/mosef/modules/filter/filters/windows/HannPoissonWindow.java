package dk.jonaslindstrom.mosef.modules.filter.filters.windows;

public class HannPoissonWindow implements Window {

  private int length;
  private double alpha;

  public HannPoissonWindow(int length, double alpha) {
    this.length = length;
    this.alpha = alpha;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public float getCoefficient(int k) {
    int m = (this.length - 1) / 2;

    // No need to use wavetable - windows are just called once when creating a filter.
    return (float) (0.5f * (1 - Math.cos(Math.PI * k / m)) * Math.exp(alpha * Math.abs(k - m) / m));
  }

}
