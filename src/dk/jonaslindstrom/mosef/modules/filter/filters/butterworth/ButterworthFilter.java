package dk.jonaslindstrom.mosef.modules.filter.filters.butterworth;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.filter.filters.IIRFilter;
import dk.jonaslindstrom.mosef.modules.Module;

import static java.lang.Math.*;

public class ButterworthFilter extends IIRFilter {

    public ButterworthFilter(MOSEFSettings settings, Module input, int order, double cutoff) {
        super(settings, input, computeCoefficients(order, settings.getSampleRate(), cutoff));
    }

    public static double[][] computeCoefficients(int order, int Fs, double cutoff) {

        double γ = tan(PI * cutoff / Fs);

        switch (order) {
            case 1: {
                double D = γ + 1;
                double b_0 = γ / D;
                double[] b = new double[] {b_0, b_0};

                double a_0 = (γ - 1) / D;
                double[] a = new double[] {a_0};

                return new double[][] {a, b};
            }

            case 2: {
                double γSquared = γ * γ;

                double sqrtTwo = sqrt(2);

                double D = γSquared + sqrtTwo * γ + 1;

                double b0 = γSquared / D;
                double[] b = new double[] {b0, 2 * b0, b0};

                double a1 = 2 * (γSquared - 1);
                double a2 = γSquared - sqrtTwo * γ + 1;
                double[] a = new double[] {a1 / D, a2 / D};

                return new double[][] {a, b};
            }

            case 3: {
                double γSquared = γ * γ;
                double γCubed = γSquared * γ;

                double D = γCubed + 2 * γSquared + 2 * γ + 1;

                double b0 = γCubed / D;
                double b1 = b0 * 3;
                double[] b = new double[] {b0, b1, b1, b0};

                double a1 = 3 * γCubed + 2 * γSquared - 2 * γ - 3;
                double a2 = 3 * γCubed - 2 * γSquared - 2 * γ + 3;
                double a3 = γCubed - 2 * γSquared + 2 * γ - 1;
                double[] a = new double[] {a1 / D, a2 / D, a3 / D};

                return new double[][] {a, b};
            }

            case 4: {
                double α = -2 * (cos(5 * PI / 8) + cos(7 * PI / 8));
                double β = 2 * (1 + 2 * cos(5 * PI / 8) * cos(7 * PI / 8));

                double γSquared = γ * γ;
                double γCubed = γSquared * γ;
                double γFourth = γCubed * γ;

                double D = γFourth + α * γCubed + β * γSquared + α * γ + 1;

                double b0 = γFourth / D;
                double b1 = 4 * b0;
                double b2 = 6 * b0;
                double[] b = new double[] {b0, b1, b2, b1, b0};

                double a1 = 2 * (2 * γFourth + α * γCubed - α * γ - 2) / D;
                double a2 = 2 * (3 * γFourth - β * γSquared + 3) / D;
                double a3 = 2 * (2 * γFourth - α * γCubed + α * γ - 2) / D;
                double a4 = (γFourth - α * γCubed + β * γSquared - α * γ + 1) / D;
                double[] a = new double[] {a1, a2, a3, a4};

                return new double[][] {a, b};
            }

            default:
                throw new UnsupportedOperationException("Unsupported order: " + order);

        }

    }

}
