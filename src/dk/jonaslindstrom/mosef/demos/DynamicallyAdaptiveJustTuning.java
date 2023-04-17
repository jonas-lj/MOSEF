package dk.jonaslindstrom.mosef.demos;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.oscillator.VCO;
import dk.jonaslindstrom.mosef.modules.oscillator.waves.PerfectSineWave;
import dk.jonaslindstrom.mosef.modules.output.OutputModule;
import dk.jonaslindstrom.mosef.modules.output.ToWaveFile;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.ArrangementTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.DynamicArrangementTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.dynamic.StepTuningFunction;
import dk.jonaslindstrom.mosef.modules.tuning.tuningfunction.relative.FiveLimitTuning;

import java.io.File;
import java.util.Arrays;

public class DynamicallyAdaptiveJustTuning {

    public static void main(String[] arguments) throws InterruptedException {

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

        double[][] frequencies = tuningFunction.getFrequencies(new int[][] {track1, track2, track3});

        for (int i = 0; i < 3; i++) {
            System.out.println(Arrays.toString(frequencies[i]));
        }

        double stepTime = 0.5;

        Module f1 = m.sample(stretch(frequencies[0], (int) (settings.getSampleRate() * stepTime)));
        Module f2 = m.sample(stretch(frequencies[1], (int) (settings.getSampleRate() * stepTime)));
        Module f3 = m.sample(stretch(frequencies[2], (int) (settings.getSampleRate() * stepTime)));
        Module melody = m.amplifier(new VCO(settings, f1, new PerfectSineWave()), 0.1);
        Module voice = m.amplifier(new VCO(settings, f2, new PerfectSineWave()), 0.1);
        Module III = m.amplifier(new VCO(settings, f3, new PerfectSineWave()), 0.1);

        OutputModule toFile = new ToWaveFile(settings, m.mixer(melody, voice, III), 16, new File("test_just.wav"));
        toFile.start();
    }

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
