package me.sniggle.security.common;

/**
 * Created by iulius on 28/06/15.
 *
 * Enumeration of supported parameters by TOTP (as defined in Google Authenticator)
 */
public enum Parameter {
  SECRET(true, true, null, null),
  ISSUER(false, false, null, null),
  ALGORITHM(false, false, new String[]{"SHA1", "SHA256", "SHA512"}, "SHA1"),
  DIGITS(false, false, new String[]{"6","8"}, "6"),
  COUNTER(false, true, null, null),
  PERIOD(false, true, null, null);

  private boolean mandatoryTOTP;
  private boolean mandatoryHOTP;
  private String[] allowedValues;
  private String defaultValue;

  /**
   * constructor
   *
   * @param mandatoryTOTP
   *   indicator whether this parameter is mandatory for TOTP protocol
   * @param mandatoryHOTP
   *   indicator whether this parameter is mandatory for HOTP protocol
   * @param allowedValues
   *   the allowed values for this parameter, null if no restriction
   * @param defaultValue
   *   the default value if defined, null otherwise
   */
  private Parameter(boolean mandatoryTOTP, boolean mandatoryHOTP, String[] allowedValues, String defaultValue) {
    this.mandatoryTOTP = mandatoryTOTP;
    this.mandatoryHOTP = mandatoryHOTP;
    this.allowedValues = allowedValues;
    this.defaultValue = defaultValue;
  }

  /**
   *
   * @return true if parameter is mandatory in TOTP
   */
  public boolean isMandatoryTOTP() {
    return mandatoryTOTP;
  }

  /**
   *
   * @return true if parameter is mandatory in HOTP
   */
  public boolean isMandatoryHOTP() {
    return mandatoryHOTP;
  }

  /**
   *
   * @return all allowed values or null if unrestricted
   */
  public String[] allowedValues() {
    return allowedValues;
  }

  /**
   *
   * @return the default value for this parameter
   */
  public String defaultValue() {
    return defaultValue;
  }

  /**
   *
   * @return the name of the parameter as it appears in the URI
   */
  public String paramName() {
    return name().toLowerCase();
  }
}
