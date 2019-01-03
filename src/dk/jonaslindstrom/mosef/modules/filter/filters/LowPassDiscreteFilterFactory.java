package dk.jonaslindstrom.mosef.modules.filter.filters;

import dk.jonaslindstrom.mosef.modules.filter.filters.windows.RectangularWindow;
import dk.jonaslindstrom.mosef.modules.filter.filters.windows.Window;

/**
 * This filter is constructed by adding waves of frequencies smaller than the cutoff frequency. This
 * is simplified using the Dirichlet kernel and l'Hopitals equality to the simpler form presented
 * below.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class LowPassDiscreteFilterFactory implements DiscreteFilterFactory {

  private int cutoff;
  private int length;
  private int samplerate;
  private Window window;

  public LowPassDiscreteFilterFactory(int cutoff, int length, int samplerate, Window window) {
    this.cutoff = cutoff;
    this.length = length;
    this.samplerate = samplerate;
    this.window = window;

  }

  public LowPassDiscreteFilterFactory(int cutoff, int length, int samplerate) {
    this(cutoff, length, samplerate, new RectangularWindow(length));
  }

  @Override
  public double[] getSamples() {
    int shift = (length - 1) / 2;

    double[] a = new double[length];
    for (int i = 0; i < a.length; i++) {
      if (i != shift) {
        a[i] = (double) (Math.sin(2 * Math.PI * cutoff * (i - shift) / samplerate)
            / (Math.PI * (i - shift)));
      } else {
        a[i] = 2.0f * cutoff / samplerate;
      }
      a[i] = a[i] * window.getCoefficient(i);
    }

    return a;
  }

}
