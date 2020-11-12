package dk.jonaslindstrom.mosef.modules.arpeggio;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;

public class Arpeggio implements Module {

  private final Module speed;
  private final Module[] tones;
  private int currentTone;
  private double t;
  private final MOSEFSettings settings;

  public Arpeggio(MOSEFSettings settings, Module speed, Module... tones) {
    this.speed = speed;
    this.settings = settings;
    this.tones = tones;

    this.currentTone = 0;
    this.t = 0.0;
  }

  @Override
  public double[] getNextSamples() {

    double[] buffer = new double[settings.getBufferSize()];

    double[] speed = this.speed.getNextSamples();

    double[][] tones = new double[this.tones.length][];
    for (int j = 0; j < tones.length; j++) {
      tones[j] = this.tones[j].getNextSamples();
    }

    for (int i = 0; i < buffer.length; i++) {
      t += speed[i] / settings.getSampleRate();

      if (t >= 1.0) {
        currentTone = (currentTone + 1) % tones.length;
        t = 0.0;
      }
      buffer[i] = tones[currentTone][i];
    }
    return buffer;
  }

}
