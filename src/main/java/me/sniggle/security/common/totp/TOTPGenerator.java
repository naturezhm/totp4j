package me.sniggle.security.common.totp;

import me.sniggle.security.common.HashAlgorithm;
import me.sniggle.security.common.encoder.Encoder;
import me.sniggle.security.common.encoder.EncoderFactory;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * Created by iulius on 25/06/15.
 */
public class TOTPGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(TOTPGenerator.class);

  private SecureRandom secureRandom;
  private long epoch;
  private long interval = 30_000;
  private long tokenLength = 6L;
  private HashAlgorithm algorithm = HashAlgorithm.HMAC_SHA1;

  public TOTPGenerator() {
    super();
    secureRandom = new SecureRandom();
  }

  public void init() {
    LOGGER.trace("init()");
    init(0L,30_000L, HashAlgorithm.HMAC_SHA1, 6);
  }

  public void init(long epoch, long interval, HashAlgorithm hashAlgorithm, long tokenLength) {
    LOGGER.trace("init(long, long, HashAlgorithm, long)");
    LOGGER.debug("Epoch: {}", epoch);
    LOGGER.debug("Interval: {}", interval);
    LOGGER.debug("Algorithm: {}", hashAlgorithm);
    LOGGER.debug("Token Length: {}", tokenLength);
    this.epoch = epoch;
    this.interval = interval;
    this.algorithm = hashAlgorithm;
    this.tokenLength = tokenLength;
  }

  private long generateCounter() {
    LOGGER.trace("generateCounter()");
    return generateCounter(System.currentTimeMillis());
  }

  private long generateCounter(long unixTimestamp) {
    LOGGER.trace("generateCounter(long)");
    return unixTimestamp / interval;
  }

  private byte[] generateCounterHex(long counter) {
    LOGGER.trace("generateCounterHex(long)");
    LOGGER.debug("Counter: {}", counter);
    byte[] result = null;
    try {
      String hex = Long.toHexString(counter);
      while( hex.length() < 16 ) {
        hex = "0" + hex;
      }
      result = Hex.decodeHex(
          hex.toCharArray()
      );
    } catch(Exception e) {
      LOGGER.error("Error: {}", e.getMessage());
    }
    return result;
  }

  public byte[] generateSecret() {
    LOGGER.trace("generateSecret()");
    byte[] secretBytes = new byte[algorithm.keyLength()];
    secureRandom.nextBytes(secretBytes);
    return secretBytes;
  }

  public String generateOTP(byte[] key) {
    LOGGER.trace("generateOTP(byte[])");
    return generateOTP(key, System.currentTimeMillis());
  }

  public String generateOTP(byte[] key, long unixTimestamp) {
    LOGGER.trace("generateOTP(byte[], long)");
    return generateOTP(key, generateCounterHex(generateCounter(unixTimestamp)));
  }

  public String generateOTP(byte[] key, byte[] message ) {
    LOGGER.trace("generateOTP(byte[], byte[])");
    // put selected bytes into result int
    Encoder encoder = EncoderFactory.getEncorder(algorithm);
    byte[] hash = encoder.generateHash(key, message);
    int offset = hash[hash.length - 1] & 0xf;

    int binary = ((hash[offset] & 0x7f) << 24)
        | ((hash[offset + 1] & 0xff) << 16)
        | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

    int intResult = binary % Double.valueOf(Math.pow(10, tokenLength)).intValue();
    String result = Integer.toString(intResult);
    while( result.length() < tokenLength ) {
      result = "0" + result;
    }
    LOGGER.debug("OTP: {}", result);
    return result;
  }

}
