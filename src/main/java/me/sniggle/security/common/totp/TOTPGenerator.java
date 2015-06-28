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
 *
 * The generator of the TOTP token, calculates the current token value based on the provided parameters
 */
public class TOTPGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(TOTPGenerator.class);

  private SecureRandom secureRandom;
  private long epoch;
  private long interval = 30_000;
  private long tokenLength = 6L;
  private HashAlgorithm algorithm = HashAlgorithm.HMAC_SHA1;

  /**
   * constructor
   */
  public TOTPGenerator() {
    super();
    secureRandom = new SecureRandom();
  }

  /**
   * initializer with default parameters
   */
  public void init() {
    LOGGER.trace("init()");
    init(0L,30_000L, HashAlgorithm.HMAC_SHA1, 6);
  }

  /**
   * initializer overriding the default values
   *
   * @param epoch
   *  the epoch to use in milliseconds (default: 0)
   * @param interval
   *  the interval used for counter calculation in milliseconds (default: 30000)
   * @param hashAlgorithm
   *  the hash algorithm to use (default: HmacSHA1)
   * @param tokenLength
   *  the length of the calculated token (default: 6)
   */
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

  /**
   * calculates the counter based on the currents system time
   *
   * @return the calculated counter
   */
  private long generateCounter() {
    LOGGER.trace("generateCounter()");
    return generateCounter(System.currentTimeMillis());
  }

  /**
   * generates the counter based on an arbitrary point in time
   *
   * @param unixTimestamp
   *  the unix timestamp in milliseconds
   * @return the counter based on the timestamp, epoch and interval
   */
  private long generateCounter(long unixTimestamp) {
    LOGGER.trace("generateCounter(long)");
    return (unixTimestamp-epoch) / interval;
  }

  /**
   * converts a long counter value to the hex binary representation
   *
   * @param counter
   * the counter to convert
   * @return the binary hex representation of the counter
   */
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

  /**
   * generates the commonly shared secret. Its length is being based on the used algorithm
   *
   * @return the secret to share between TOTP parties
   */
  public byte[] generateSecret() {
    LOGGER.trace("generateSecret()");
    byte[] secretBytes = new byte[algorithm.keyLength()];
    secureRandom.nextBytes(secretBytes);
    return secretBytes;
  }

  /**
   * generates the current TOTP token
   *
   * @param key
   *   the commonly shared secret as binary hex representation
   * @return the TOTP token
   */
  public String generateOTP(byte[] key) {
    LOGGER.trace("generateOTP(byte[])");
    return generateOTP(key, System.currentTimeMillis());
  }

  /**
   * generates the TOTP token for an arbitrary moment in time
   *
   * @param key
   *  the commonly shared secret as binary hex representation
   * @param unixTimestamp
   *  the unix timestamp in milliseconds
   * @return the TOTP token
   */
  public String generateOTP(byte[] key, long unixTimestamp) {
    LOGGER.trace("generateOTP(byte[], long)");
    return generateOTP(key, generateCounterHex(generateCounter(unixTimestamp)));
  }

  /**
   * generates a hash code for a commonly shared secret and arbitrary binary data
   * @param key
   *  the commonly shared secret
   * @param message
   *  the message to hash
   * @return the hash code for key and message
   */
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
