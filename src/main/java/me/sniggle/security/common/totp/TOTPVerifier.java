package me.sniggle.security.common.totp;

import me.sniggle.security.common.HashAlgorithm;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iulius on 26/06/15.
 *
 * Verifies the presented TOTP token
 */
public class TOTPVerifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(TOTPVerifier.class);

  private final long gracePeriod;
  private TOTPGenerator generator;

  /**
   * constructor
   */
  public TOTPVerifier() {
    this(5_000L);
  }

  /**
   * constructor
   *
   * @param gracePeriod
   *  the grace period used to accept the previous interval token
   */
  public TOTPVerifier( long gracePeriod ) {
    generator = new TOTPGenerator();
    generator.init();
    this.gracePeriod = gracePeriod;
  }

  /**
   * constructor
   *
   * @param gracePeriod
   *   the grace period to accept the previous token
   * @param epoch
   *   the epoch to use in ms (default: 0)
   * @param interval
   *   the interval to use in ms (default: 30000)
   * @param hashAlgorithm
   *   the hash algorithm to use (default: HmacSHA1)
   * @param tokenLength
   *   the expected token length (default: 6)
   */
  public TOTPVerifier(long gracePeriod, long epoch, long interval, HashAlgorithm hashAlgorithm, int tokenLength) {
    generator = new TOTPGenerator();
    generator.init(epoch, interval, hashAlgorithm, tokenLength);
    this.gracePeriod = gracePeriod;
  }

  /**
   * verifies the presented token using the current time and presented shared secret
   *
   * @param sharedSecret
   *   the commonly shared secret
   * @param presentedToken
   *   the TOTP toke to verify
   * @return true if successful otherwise false
   */
  public boolean verify(String sharedSecret, String presentedToken) {
    LOGGER.trace("verify(String, String)");
    return verify(sharedSecret, presentedToken, System.currentTimeMillis());
  }

  /**
   * verifies whether the token was/is/will be valid at a given point in time
   *
   * @param sharedSecret
   *   the commonly shared secret
   * @param presentedToken
   *   the presented TOTP token
   * @param unixTimestamp
   *   the point in time to refer to (unix timestamp in ms)
   * @return true if verification is successful, else false
   */
  public boolean verify(String sharedSecret, String presentedToken, long unixTimestamp) {
    LOGGER.trace("verify(String, String, long)");
    LOGGER.debug("OTP (input): {}", presentedToken);
    boolean result = false;
    long graceTimestamp = unixTimestamp - gracePeriod;
    try {
      byte[] sharedSecretBytes = new Base32().decode(sharedSecret);
      String expectedKey = generator.generateOTP(sharedSecretBytes, unixTimestamp);
      LOGGER.debug("OTP (expected): {}", expectedKey);
      if (expectedKey.equals(presentedToken)) {
        LOGGER.info("OTP verified");
        result = true;
      } else {
        LOGGER.info("Checking grace-period OTP");
        expectedKey = generator.generateOTP(sharedSecretBytes, graceTimestamp);
        if (expectedKey.equals(presentedToken)) {
          LOGGER.info("OTP (grace-period) verified");
          result = true;
        }
      }
    }catch(Exception e){
      LOGGER.error("Error: {}", e.getMessage());
    }
    return result;
  }

}
