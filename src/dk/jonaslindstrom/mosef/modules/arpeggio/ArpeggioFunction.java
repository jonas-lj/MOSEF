package dk.jonaslindstrom.mosef.modules.arpeggio;

public abstract class ArpeggioFunction {

  private int n;

  public ArpeggioFunction(int n) {
    this.n = n;
  }

  protected int getTones() {
    return n;
  }

  abstract int getNextTone();

}
