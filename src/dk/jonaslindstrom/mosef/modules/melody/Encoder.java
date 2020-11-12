package dk.jonaslindstrom.mosef.modules.melody;

import java.io.IOException;

public interface Encoder {

  String encode(Track track) throws IOException;
  Track decode(String string) throws IOException, ClassNotFoundException;

}
