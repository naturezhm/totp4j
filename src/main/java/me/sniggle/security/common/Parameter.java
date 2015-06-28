package me.sniggle.security.common;

/**
 * Created by iulius on 28/06/15.
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

  private Parameter(boolean mandatoryTOTP, boolean mandatoryHOTP, String[] allowedValues, String defaultValue) {
    this.mandatoryTOTP = mandatoryTOTP;
    this.mandatoryHOTP = mandatoryHOTP;
    this.allowedValues = allowedValues;
    this.defaultValue = defaultValue;
  }

  public boolean isMandatoryTOTP() {
    return mandatoryTOTP;
  }

  public boolean isMandatoryHOTP() {
    return mandatoryHOTP;
  }

  public String[] allowedValues() {
    return allowedValues;
  }

  public String defaultValue() {
    return defaultValue;
  }

  public String paramName() {
    return name().toLowerCase();
  }
}
