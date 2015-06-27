package me.sniggle.security.common.totp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

/**
 * Created by iulius on 26/06/15.
 *
 * GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZA====
 * GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQGEZDGNA=
 */
public class TOTPVerifierSteps {

  private TOTPVerifier totpVerifier;
  private String commonSecret;
  private boolean actualResult;

  @Given("^I have an instance of the TOTPVerifier with a grace period of (\\d+)s$")
  public void i_have_an_instance_of_the_TOTPVerifier_with_a_grace_period_of_s(long gracePeriod) throws Throwable {
    totpVerifier = new TOTPVerifier(gracePeriod * 1000);
  }

  @Given("^I have a commonly shared secret of ([A-Z0-9]+)$")
  public void i_have_a_commonly_shared_secret_of_GEZDGNBVGY_TQOJQGEZDGNBVGY_TQOJQ(String secret) throws Throwable {
    commonSecret = secret;
  }

  @When("^I try to verify the (\\d+) for the given (\\d+) timestamp$")
  public void i_try_to_verify_the_for_the_given_timestamp(String totpCode, long timestamp) throws Throwable {
    actualResult = totpVerifier.verify(commonSecret, totpCode, timestamp*1000);
  }

  @Then("^I expect it to (be successful|fail)$")
  public void i_expect_it_to_be_successful(String expectedResult) throws Throwable {
    assertEquals(!"fail".equals(expectedResult), actualResult);
  }
  
}
