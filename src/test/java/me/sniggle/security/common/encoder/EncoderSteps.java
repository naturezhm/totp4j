package me.sniggle.security.common.encoder;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import me.sniggle.security.common.HashAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import static me.sniggle.security.common.HelperUtils.generateSecret;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by iulius on 27/06/15.
 */
public class EncoderSteps {

  private String result;
  private Encoder encoder;
  private HashAlgorithm algorithm;
  private String sharedSecret;

  @Given("^I have an encoder for (HMAC_SHA1|HMAC_SHA256|HMAC_SHA512)$")
  public void i_have_an_encoder_for_HMAC_SHA(String algorithm) throws Throwable {
    i_have_an_encoder_for_HMAC_SHA(algorithm, null);
  }

  @Given("^I have an encoder for (HMAC_SHA1|HMAC_SHA256|HMAC_SHA512) and the shared secret (.+)$")
  public void i_have_an_encoder_for_HMAC_SHA(String algorithm, String sharedSecret) throws Throwable {
    this.algorithm = HashAlgorithm.valueOf(algorithm);
    encoder = EncoderFactory.getEncorder(this.algorithm);
    this.sharedSecret = sharedSecret;
  }

  @When("^I encode ([a-zA-Z0-9\\.]+) using the encoder$")
  public void i_encode_wikipedia_org_using_the_encoder(String value) throws Throwable {
    byte[] resultBytes = encoder.generateHash(sharedSecret.getBytes(), value.getBytes());
    result = resultBytes == null ? null : Hex.encodeHexString(resultBytes);
  }

  @Then("^I expected this ([a-zA-Z0-9]+) as result$")
  public void i_expected_this_xxxxxx_as_result(String result) throws Throwable {
    assertEquals(result, this.result);
  }

  @When("^I try to encode a null value using the encoder$")
  public void i_try_to_encode_a_null_value_using_the_encoder() throws Throwable {
    result = encoder.generateHash(generateSecret(algorithm), null) == null ? null : "";
  }

  @Then("^I expect the encoder to return a null value$")
  public void i_expect_the_encoder_to_return_a_null_value() throws Throwable {
    assertNull(result);
  }
}
