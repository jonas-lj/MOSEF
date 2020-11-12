package dk.jonaslindstrom.mosef.modules.filter.filters;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;
import dk.jonaslindstrom.mosef.modules.filter.filters.butterworth.ButterworthFilter;

public class VCF extends SimpleModule {

  private final SampleMemory x, y;
  private final double[][] a, b;
  private final double min, delta;

  public VCF(MOSEFSettings settings, Module input, Module cutoff) {
    super(settings, input, cutoff);

    int order = 3;
    int size = 10000;

    x = new SampleMemory(order + 1);
    y = new SampleMemory(order);

    a = new double[size][];
    b = new double[size][];

    this.min = 1.0;
    double max = settings.getSampleRate() / 2.0;
    this.delta = (max - min) / size;

    for (int j = 0; j < size; j++) {
      double[][] c = ButterworthFilter.computeCoefficients(order,
          settings.getSampleRate(), min + delta * j);
      a[j] = c[0];
      b[j] = c[1];
    }
  }

  @Override
  public double getNextSample(double[] inputs) {

    int j = (int) ((inputs[1] - min) / delta);

    x.push(inputs[0]);

    double c = 0;

    for (int i = 0; i < b[j].length; i++) {
      c += b[j][i] * x.get(i);
    }

    for (int i = 0; i < a[j].length; i++) {
      c -= a[j][i] * y.get(i);
    }

    y.push(c);

    return c;
  }
}
