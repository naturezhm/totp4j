package me.sniggle.security.common.encoder;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import me.sniggle.security.common.HashAlgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by iulius on 27/06/15.
 */
public class EncoderFactorySteps {

  private Encoder actualEncoder;
  private Class<?> expectedEncoder;

  @When("^I request an Encoder for (HMAC_SHA1|HMAC_SHA256|HMAC_SHA512) from the Encoder Factory$")
  public void i_request_an_Encoder_for_HMAC_SHA_from_the_Encoder_Factory(String codec) throws Throwable {
    actualEncoder = EncoderFactory.getEncorder(HashAlgorithm.valueOf(codec));
    if( "HMAC_SHA1".equals(codec) ) {
      expectedEncoder = HmacSha1Encoder.class;
    } else if( "HMAC_SHA256".equals(codec) ) {
      expectedEncoder = HmacSha256Encoder.class;
    } else {
      expectedEncoder = HmacSha512Encoder.class;
    }
  }

  @Then("^I expect the appropriate Encoder$")
  public void i_expect_the_appropriate_Encoder() throws Throwable {
    assertEquals(expectedEncoder, actualEncoder.getClass());
  }

  @When("^I request an Encoder for null$")
  public void i_request_an_Encoder_for_null() throws Throwable {
    actualEncoder = EncoderFactory.getEncorder(null);
  }

  @Then("^I expect no Encoder to be returned$")
  public void i_expect_no_Encoder_to_be_returned() throws Throwable {
    assertNull(expectedEncoder);
  }

}
