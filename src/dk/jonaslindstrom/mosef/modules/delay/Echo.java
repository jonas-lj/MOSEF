package dk.jonaslindstrom.mosef.modules.delay;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Map;

public class Echo extends CompositeModule {

  private Module input;
  private int echos;
  private Module time;
  private double max;

  public Echo(MOSEF mosef, Module input, int echos, Module time, double max) {
    super(mosef);
    this.input = input;
    this.time = time;
    this.echos = echos;
    this.max = max;
  }

  @Override
  public Module buildModule(MOSEF m) {
    Module[] split = m.split(input, echos + 1);
    Module[] times = m.split(time, echos);
    Module[] delays = new Module[echos];
    for (int i = 1; i <= echos; i++) {
      Module delay =
          m.amplifier(m.delay(split[i], m.amplifier(times[i - 1], i), max * i), 0.2f);
      delays[i - 1] = delay;
    }
    return m.mixer(split[0], m.mixer(delays));
  }

  @Override
  public Map<String, Module> getInputs() {
    return Map.of("In", input, "Time", time);
  }

}
