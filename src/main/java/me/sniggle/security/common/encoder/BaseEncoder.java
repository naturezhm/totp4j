package me.sniggle.security.common.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by iulius on 26/06/15.
 */
public abstract class BaseEncoder implements Encoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseEncoder.class);

  private String algorithm;

  protected BaseEncoder(String algorithm) {
    super();
    this.algorithm = algorithm;
  }

  public byte[] generateHash(byte[] key, byte[] message) {
    LOGGER.trace("generateHash(byte[], byte[])");
    byte[] result = null;
    if( key != null && message != null ) {
      try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "RAW");
        LOGGER.debug("Using hash algorithm: {}",algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        result = mac.doFinal(message);
      } catch( Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
      }
    }
    return result;
  }
}
