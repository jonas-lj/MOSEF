package dk.jonaslindstrom.mosef.modules.oscillator;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class ExactOscillator implements MOSEFModule {

  private MOSEFSettings settings;
  private int halfWaveLength;
  private int i;
  private boolean low;
  private float[] buffer;

  public ExactOscillator(MOSEFSettings settings, int halfWaveLength) {
    this.settings = settings;
    this.halfWaveLength = halfWaveLength;
    this.i = 0;
    this.low = true;
    this.buffer = new float[settings.getBufferSize()];
  }
  
  @Override
  public float[] getNextSamples() {
    for (int j = 0; j < settings.getBufferSize(); j++) {
      buffer[j] = low ? -.99f : .99f;
      i++;
      if (i == halfWaveLength) {
        i = 0;
        low = !low;
      }
    }
    return buffer;
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of();
  }

}
