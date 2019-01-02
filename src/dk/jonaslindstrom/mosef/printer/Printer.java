package dk.jonaslindstrom.mosef.printer;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;
import java.util.Collections;
import java.util.Map;

public class Printer {

  private MOSEFModule out;

  public Printer(MOSEFModule out) {
    this.out = out;
  }

  private String print(MOSEFModule base, int indention) {
    StringBuilder sb = new StringBuilder();
    // sb.append(String.join("", Collections.nCopies(indention, " ")));
    sb.append(base.getClass().getSimpleName());
    Map<String, MOSEFModule> inputs = base.getInputs();
    for (String name : inputs.keySet()) {
      sb.append("\n");
      sb.append(String.join("", Collections.nCopies(indention, " ")));
      sb.append(" - ");
      sb.append(name);
      sb.append("<-");
      sb.append(print(inputs.get(name), indention + 1));
    }
    return sb.toString();
  }

  public String print() {
    return print(out, 0);
  }

}
