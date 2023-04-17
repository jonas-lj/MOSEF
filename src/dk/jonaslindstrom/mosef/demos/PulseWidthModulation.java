package dk.jonaslindstrom.mosef.demos;

import dk.jonaslindstrom.mosef.MOSEF;
import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.arpeggio.Arpeggio;
import java.util.concurrent.TimeUnit;

/**
 * This application tests pulse width modulation synthesis where the width of a pulse wave is
 * modulated by an LFO.
 * 
 * @author Jonas Lindstrøm (mail@jonaslindstrom.dk)
 *
 */
public class PulseWidthModulation {

  public static void main(String[] arguments) throws InterruptedException {

    MOSEFSettings settings = new MOSEFSettings(8000, 256, 16);
    MOSEF m = new MOSEF(settings);

    Module arpeggio = new Arpeggio(settings, m.constant(50.0f),
        m.constants(440.0f, 440.0f * 6.0f / 5.0f, 440.0f * 3.0f / 2.0f));
    Module modulator = m.offset(m.sine(15.0), 0.3, 0.1);
    Module oscillator =
        m.pulse(arpeggio, modulator);
    Module out = m.amplifier(oscillator, 0.2f);

    m.audioOut(out);
    m.start();

    TimeUnit.SECONDS.sleep(30);

    m.stop();
  }

}
