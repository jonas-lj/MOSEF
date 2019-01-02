package dk.jonaslindstrom.mosef.modules.delay;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Map;

public class Echo extends CompositeModule {

  private MOSEFModule input;
  private int echos;
  private MOSEFModule time;
  private float max;

  public Echo(MOSEF mosef, MOSEFModule input, int echos, MOSEFModule time, float max) {
    super(mosef);
    this.input = input;
    this.time = time;
    this.echos = echos;
    this.max = max;
  }

  @Override
  public MOSEFModule buildModule(MOSEF m) {
    MOSEFModule[] split = m.split(input, echos + 1);
    MOSEFModule[] times = m.split(time, echos);
    MOSEFModule[] delays = new MOSEFModule[echos];
    for (int i = 1; i <= echos; i++) {
      MOSEFModule delay =
          m.amplifier(m.delay(split[i], m.amplifier(times[i - 1], i), max * i), 0.2f);
      delays[i - 1] = delay;
    }
    return m.mixer(split[0], m.mixer(delays));
  }

  @Override
  public Map<String, MOSEFModule> getInputs() {
    return Map.of("In", input, "Time", time);
  }

}
