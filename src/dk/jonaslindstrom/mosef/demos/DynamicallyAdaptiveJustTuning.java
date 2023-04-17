package dk.jonaslindstrom.mosef.demos;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.oscillator.VCO;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.PerfectSineWave;
import dk.jonaslindstrom.mosef.modules.output.ToWaveFile;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.ArrangementTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.DynamicArrangementTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.StepTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative.FiveLimitTuning;

import java.io.File;

public class DynamicallyAdaptiveJustTuning {

    public static void main(String[] arguments) {

        MOSEFSettings settings = new MOSEFSettings(44100, 1024, 16);
        MOSEF m = new MOSEF(settings);

        int[] track1 = new int[]{76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 76, 81, 77, 74, 72, 71, 72, 71, 71, 71, 69, 67, 67, 67, 67};
        int[] track2 = new int[]{67, 67, 67, 67, 67, 67, 67, 67, 72, 72, 72, 72, 72, 72, 72, 72, 69, 69, 72, 72, 71, 69, 67, 66, 67, 67, 62, 62, 59, 59, 62, 62};
        int[] track3 = new int[]{60, 60, 60, 60, 59, 59, 59, 59, 57, 57, 57, 57, 55, 55, 55, 55, 53, 53, 53, 53, 54, 54, 54, 54, 55, 55, 55, 55, 53, 53, 53, 53};

        ArrangementTuningFunction tuningFunction = new DynamicArrangementTuningFunction(
                new StepTuningFunction(660.0, new FiveLimitTuning()),
                new FiveLimitTuning());

        // Uncomment to use equal tuning
//        ArrangementTuningFunction tuningFunction = new DynamicArrangementTuningFunction(
//                new SimpleTrackTuningFunction(new EquallyTemperedTuningFunction()),
//                new EqualTemperment(12));

        double[][] frequencies = tuningFunction.getFrequencies(new int[][]{track1, track2, track3});

        // The time for each step (8th note) in seconds
        double stepTime = 0.5;

        Module[] voices = new Module[3];
        for (int i = 0; i < 3; i++) {
            Module f = m.sample(stretch(frequencies[i], (int) (settings.getSampleRate() * stepTime)));
            voices[i] = m.amplifier(new VCO(settings, f, new PerfectSineWave()), 0.1);
        }

        ToWaveFile toWaveFile = new ToWaveFile(settings, m.mixer(voices), new File("test_just.wav"));
        toWaveFile.write(16);
    }

    /**
     * Stretches an array by a given factor.
     */
    public static double[] stretch(double[] input, int stretch) {
        double[] output = new double[input.length * stretch];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < stretch; j++) {
                output[i * stretch + j] = input[i];
            }
        }
        return output;
    }

}
