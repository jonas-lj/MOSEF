package dk.jonaslindstrom.mosef.modules.tuning;

import dk.jonaslindstrom.mosef.modules.Module;

public interface Tuner extends Module {

  public void setNote(int note);

  public int getNote();
}
