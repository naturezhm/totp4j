package me.sniggle.security.common.totp;

import me.sniggle.security.common.HashAlgorithm;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iulius on 26/06/15.
 */
public class TOTPVerifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(TOTPVerifier.class);

  private final long gracePeriod;
  private TOTPGenerator generator;

  public TOTPVerifier() {
    this(5_000L);
  }

  public TOTPVerifier( long gracePeriod ) {
    generator = new TOTPGenerator();
    generator.init();
    this.gracePeriod = gracePeriod;
  }

  public TOTPVerifier(long gracePeriod, long epoch, long interval, HashAlgorithm hashAlgorithm, int tokenLength) {
    generator = new TOTPGenerator();
    generator.init(epoch, interval, hashAlgorithm, tokenLength);
    this.gracePeriod = gracePeriod;
  }

  public boolean verify(String sharedSecret, String actualKey) {
    LOGGER.trace("verify(String, String)");
    return verify(sharedSecret, actualKey, System.currentTimeMillis());
  }

  public boolean verify(String sharedSecret, String actualKey, long unixTimestamp) {
    LOGGER.trace("verify(String, String, long)");
    LOGGER.debug("OTP (input): {}", actualKey);
    boolean result = false;
    long graceTimestamp = unixTimestamp - gracePeriod;
    try {
      byte[] sharedSecretBytes = new Base32().decode(sharedSecret);
      String expectedKey = generator.generateOTP(sharedSecretBytes, unixTimestamp);
      LOGGER.debug("OTP (expected): {}", expectedKey);
      if (expectedKey.equals(actualKey)) {
        LOGGER.info("OTP verified");
        result = true;
      } else {
        LOGGER.info("Checking grace-period OTP");
        expectedKey = generator.generateOTP(sharedSecretBytes, graceTimestamp);
        if (expectedKey.equals(actualKey)) {
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
