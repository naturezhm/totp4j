package me.sniggle.security.common.totp;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import me.sniggle.security.common.HashAlgorithm;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static me.sniggle.security.common.HelperUtils.generateSecret;
import static org.junit.Assert.assertEquals;

/**
 * Created by iulius on 26/06/15.
 *
 * Based on this http://www.cnblogs.com/dyingbleed/archive/2012/12/05/2803782.html
 */
public class TOTPGeneratorSteps {

  private TOTPGenerator generator;
  private long timestamp;
  private String totpCode;
  private HashAlgorithm algorithm;


  @Given("^I have an instance of the TOTP Generator instantiated$")
  public void i_have_an_instance_of_the_TOTP_Generator_instantiated() throws Throwable {
    generator = new TOTPGenerator();
  }

  @Given("^I have initialized this TOTP Generator with default parameters$")
  public void i_have_initialized_this_TOTP_Generator_with_default_parameters() throws Throwable {
    generator.init();
  }

  @Given("^I have initialized this TOTP Generator with the following parameters$")
  public void i_have_initialized_this_TOTP_Generator_with_the_following_parameters(List<Map<String, String>> parameters) throws Throwable {
    //convert seconds to millisecods
    long epoch = Long.valueOf(parameters.get(0).get("EPOCH")) * 1000;
    //convert seconds to milliseconds
    long interval = Long.valueOf(parameters.get(0).get("INTERVAL")) * 1000;
    algorithm = HashAlgorithm.valueOf("HMAC_" + parameters.get(0).get("ALGORITHM"));
    long tokenLength = Long.valueOf(parameters.get(0).get("TOKEN_LENGTH"));

    generator.init(epoch, interval, algorithm, tokenLength);
  }

  @Given("^I the unix timestamp used is (\\d+)$")
  public void i_the_unix_timestamp_used_is(long timestamp) throws Throwable {
    //convert seconds to milliseconds
    this.timestamp = timestamp * 1000;
  }

  @When("^I calculate the TOTP Code$")
  public void i_calculate_the_TOTP_Code() throws Throwable {
    totpCode = generator.generateOTP(generateSecret(algorithm), timestamp);
  }

  @Then("^I expect the following (\\d+)$")
  public void i_expect_the_following(String expectedTotpCode) throws Throwable {
    assertEquals(expectedTotpCode, totpCode);
  }

}
