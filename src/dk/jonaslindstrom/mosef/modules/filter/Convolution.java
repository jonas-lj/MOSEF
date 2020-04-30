package dk.jonaslindstrom.mosef.modules.filter;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.memory.SampleMemory;
import dk.jonaslindstrom.mosef.modules.Module;

public class Convolution implements Module {

    private final SampleMemory memory;
    private final Module input;
    private final double[] filter;
    private final double[] buffer;

    public Convolution(MOSEFSettings settings, Module input, double[] filter) {
        this.input = input;
        this.filter = filter;
        this.memory = new SampleMemory(filter.length);
        this.buffer = new double[settings.getBufferSize()];
    }

    @Override
    public double[] getNextSamples() {
        double[] inputs = input.getNextSamples();

        for (int i = 0; i < buffer.length; i++) {
            this.memory.push(inputs[i]);
            double c = 0.0;
            for (int j = 0; j < filter.length; j++) {
                c += memory.get(j) * filter[j];
            }
            buffer[i] = c;
        }

        return buffer;
    }
}
