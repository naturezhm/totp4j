package me.sniggle.security.common;

/**
 * Created by iulius on 25/06/15.
 */
public enum HashAlgorithm {

  HMAC_SHA1(20),
  HMAC_SHA256(32),
  HMAC_SHA512(64);

  private int keyLength;

  private HashAlgorithm(int keyLength) {
    this.keyLength = keyLength;
  }

  public int keyLength() {
    return keyLength;
  }

}
