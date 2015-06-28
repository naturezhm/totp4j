package me.sniggle.security.common.encoder;

import me.sniggle.security.common.HashAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iulius on 26/06/15.
 *
 * The factory to facilitate the selection of the right encoder
 */
public final class EncoderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(EncoderFactory.class);

  private EncoderFactory() {
    super();
  }

  /**
   * retrieval function for the suitable hash algorithm
   *
   * @param hashAlgorithm
   *  the hash algorithm as named in Java Crypto API, e.g. HmacSHA1
   * @return the appropriate encoder or null if the algorithm is not supported
   */
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
