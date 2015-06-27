package me.sniggle.security.common.encoder;

/**
 * Created by iulius on 25/06/15.
 */
public interface Encoder {

  public byte[] generateHash(byte[] key, byte[] message);

}
