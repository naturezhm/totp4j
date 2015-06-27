package me.sniggle.security.common;

import org.apache.commons.codec.binary.Hex;

/**
 * Created by iulius on 27/06/15.
 */
public final class HelperUtils {

  private HelperUtils() {}

  public static byte[] generateSecret(HashAlgorithm algorithm) {
    String suffix = "";
    if( algorithm == HashAlgorithm.HMAC_SHA256 ) {
      suffix = "313233343536373839303132";
    } else if( algorithm == HashAlgorithm.HMAC_SHA512 ) {
      suffix = "3132333435363738393031323334353637383930"
          + "3132333435363738393031323334353637383930" + "31323334";
    }
    try {
      return Hex.decodeHex(("3132333435363738393031323334353637383930" + suffix).toCharArray());
    } catch(Exception e) {}
    return null;
  }

}
