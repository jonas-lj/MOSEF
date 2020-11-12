package dk.jonaslindstrom.mosef.modules.melody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class DefaultEncoder implements Encoder {

  public String encode(Track track) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(bos);
    os.writeObject(track);

    String encoded = Base64.getEncoder().encodeToString(bos.toByteArray());
    os.close();

    return encoded;
  }

  public Track decode(String encoded) throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encoded));
    ObjectInputStream ois = new ObjectInputStream(bis);
    return (Track) ois.readObject();
  }



}
