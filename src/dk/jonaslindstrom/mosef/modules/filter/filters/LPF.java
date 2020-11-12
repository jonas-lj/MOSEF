package dk.jonaslindstrom.mosef.modules.filter.filters;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.filter.filters.butterworth.ButterworthFilter;

public class LPF extends SimpleModule {

  private final SampleMemory x, y;
  private final double[] a, b;

  public LPF(MOSEFSettings settings, Module input, double cutoff) {
    this(settings, input, 3, cutoff);
  }

  public LPF(MOSEFSettings settings, Module input, int order, double cutoff) {
    super(settings, input);

    x = new SampleMemory(order + 1);
    y = new SampleMemory(order);

    double[][] c = ButterworthFilter.computeCoefficients(order,
        settings.getSampleRate(), cutoff);
    a = c[0];
    b = c[1];
  }

  @Override
  public double getNextSample(double[] inputs) {

    x.push(inputs[0]);

    double c = 0;

    for (int i = 0; i < b.length; i++) {
      c += b[i] * x.get(i);
    }

    for (int i = 0; i < a.length; i++) {
      c -= a[i] * y.get(i);
    }

    y.push(c);

    return c;
  }
}
