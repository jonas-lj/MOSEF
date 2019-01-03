package dk.jonaslindstrom.mosef.modules.tuning;

// package dk.jonaslindstrom.mosef.modules.tuning;
//
// import dk.jonaslindstrom.mosef.MOSEFSettings;
// import dk.jonaslindstrom.mosef.modules.Module;
//
// public class Melody {
//
// private Tuner tuner;
// private int[] melody;
// private int state;
// private MOSEFSettings settings;
// private Module clock;
//
// public Melody(MOSEFSettings settings, Module clock, Tuner tuner, int[] melody) {
// this.settings = settings;
// this.clock = clock;
// this.tuner = tuner;
// this.tuner = tuner;
// this.melody = melody;
// this.state = 0;
// }
//
// @Override
// public double[] getNextSamples() {
//
// if (inputs[0] > 0.0f) {
// tuner.setNote(melody[state++]);
// if (state >= melody.length) {
// state = 0;
// }
// }
// return inputs[1];
// }
//
// }
