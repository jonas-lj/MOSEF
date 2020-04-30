package dk.jonaslindstrom.mosef.modules.arpeggio;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Arpeggio implements Module {

  private Module speed;
  private Module[] tones;
  private int currentTone;
  private double t;
  private MOSEFSettings settings;

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
