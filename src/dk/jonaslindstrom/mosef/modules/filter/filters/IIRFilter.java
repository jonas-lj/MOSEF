package dk.jonaslindstrom.mosef.modules.filter.filters;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.SimpleModule;

public class IIRFilter extends SimpleModule {

    private final double[] a, b;
    private final SampleMemory x, y;

    protected IIRFilter(MOSEFSettings settings, Module input, double[][] c) {
        this(settings, input, c[0], c[1]);
    }

    public IIRFilter(MOSEFSettings settings, Module input, double[] a, double[] b) {
        super(settings, input);

        this.a = a;
        this.b = b;

        this.x = new SampleMemory(b.length);
        this.y = new SampleMemory(a.length);
    }

    @Override
    public double getNextSample(double... inputs) {
        x.push(inputs[0]);

        double c = 0;

        for (int i = 0; i < b.length; i++) {
            c+= b[i] * x.get(i);
        }

        for (int j = 0; j < a.length; j++) {
            c -= a[j] * y.get(j);
        }

        y.push(c);

        return c;
    }
}
