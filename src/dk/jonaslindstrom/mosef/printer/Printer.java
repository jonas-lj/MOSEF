package dk.jonaslindstrom.mosef.printer;

import dk.jonaslindstrom.mosef.modules.Module;
import java.util.Collections;
import java.util.Map;

public class Printer {

  private Module out;

  public Printer(Module out) {
    this.out = out;
  }

  private String print(Module base, int indention) {
    StringBuilder sb = new StringBuilder();
    // sb.append(String.join("", Collections.nCopies(indention, " ")));
    sb.append(base.getClass().getSimpleName());
    Map<String, Module> inputs = base.getInputs();
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
