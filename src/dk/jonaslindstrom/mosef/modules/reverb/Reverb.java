package dk.jonaslindstrom.mosef.modules.reverb;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.filter.Convolution;
import dk.jonaslindstrom.mosef.modules.Module;

import java.util.Random;

public class Reverb extends Convolution {

    public Reverb(MOSEFSettings settings, Module input) {
        this(settings, input,10000, 0.995);
    }

    public Reverb(MOSEFSettings settings, Module input, int length, double r) {
        super(settings, input, buildFilter(length, r));
    }

    private static double[] buildFilter(int length, double r) {
        Random random = new Random();
        double[] filter = new double[length];
        for (int i = 0; i < length; i++) {
            filter[i] = random.nextGaussian() * r;
            r *= r;
        }
        return filter;
    }

}
