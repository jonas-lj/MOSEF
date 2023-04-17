package dk.jonaslindstrom.mosef.modules.filter.filters;

import dk.jonaslindstrom.mosef.MOSEFSettings;
import dk.jonaslindstrom.mosef.modules.CompositeModule;
import dk.jonaslindstrom.mosef.modules.Module;
import dk.jonaslindstrom.mosef.modules.amplifier.Amplifier;
import dk.jonaslindstrom.mosef.modules.amplifier.VCA;
import dk.jonaslindstrom.mosef.modules.feedback.Feedback;
import dk.jonaslindstrom.mosef.modules.misc.Constant;
import java.util.Map;

public class ResonantVCF extends CompositeModule {

  public ResonantVCF(MOSEFSettings settings,
      Module input, Module cutoff, Module resonance) {
    super(settings, Map.of("in", input, "cv", cutoff, "res", resonance));
  }

  @Override
  public Module buildModule(MOSEFSettings settings, Map<String, Module> inputs,
      Map<String, Double> parameters) {
    Feedback feedback = new Feedback(settings, inputs.get("in"), inputs.get("res"));
    Module filter = new VCF(settings, feedback, inputs.get("cv"));
    return feedback.attachFeedback(new Amplifier(settings, filter, -1.0));
  }
}
