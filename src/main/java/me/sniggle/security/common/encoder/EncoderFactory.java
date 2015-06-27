package me.sniggle.security.common.encoder;

import me.sniggle.security.common.HashAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iulius on 26/06/15.
 */
public class EncoderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(EncoderFactory.class);

  private EncoderFactory() {
    super();
  }

  public static final Encoder getEncorder(HashAlgorithm hashAlgorithm) {
    LOGGER.trace("getEncoder(HashAlgorithm)");
    LOGGER.debug("Hash Algorithm: {}", hashAlgorithm);
    Encoder result = null;
    if( hashAlgorithm != null ) {
      switch (hashAlgorithm) {
        case HMAC_SHA1:
          result = new HmacSha1Encoder();
          break;
        case HMAC_SHA256:
          result = new HmacSha256Encoder();
          break;
        case HMAC_SHA512:
          result = new HmacSha512Encoder();
          break;
      }
    }
    return result;
  }

}
