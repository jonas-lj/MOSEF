package dk.jonaslindstrom.mosef.modules.filter.filters.windows;

public class RectangularWindow implements Window {

  private int length;

  public RectangularWindow(int length) {
    this.length = length;
  }

  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public float getCoefficient(int k) {
    if (k >= 0 && k < length) {
      return 1.0f;
    }
    return 0.0f;
  }

}
