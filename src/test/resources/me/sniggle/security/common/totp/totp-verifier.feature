Feature: TOTP Verifier
  Scenario Outline: Verify TOTP
    Given I have an instance of the TOTPVerifier with a grace period of <GRACE_PERIOD>s
      And I have a commonly shared secret of <SHARED_SECRET>
     When I try to verify the <TOTP_CODE> for the given <UNIX_TIMESTAMP> timestamp
     Then I expect it to be successful

    Examples:
      | GRACE_PERIOD | SHARED_SECRET                    | TOTP_CODE | UNIX_TIMESTAMP |
      | 5            | GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ | 287082    | 30             |
      | 5            | GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ | 287082    | 59             |
      | 5            | GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ | 287082    | 64             |

  Scenario Outline: Failed TOTP verification
    Given I have an instance of the TOTPVerifier with a grace period of <GRACE_PERIOD>s
    And I have a commonly shared secret of <SHARED_SECRET>
    When I try to verify the <TOTP_CODE> for the given <UNIX_TIMESTAMP> timestamp
    Then I expect it to fail

  Examples:
    | GRACE_PERIOD | SHARED_SECRET                    | TOTP_CODE | UNIX_TIMESTAMP |
    | 5            | GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ | 287082    | 29             |
    | 5            | GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ | 287082    | 65             |