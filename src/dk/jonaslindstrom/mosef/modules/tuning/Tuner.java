package dk.jonaslindstrom.mosef.modules.tuning;

import dk.jonaslindstrom.mosef.modules.MOSEFModule;

public interface Tuner extends MOSEFModule {

  public void setNote(int note);

  public int getNote();
}
