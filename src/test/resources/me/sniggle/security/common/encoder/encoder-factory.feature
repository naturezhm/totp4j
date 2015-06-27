Feature: Encoder Factory
  Scenario Outline: Requesting Encoder for particular algorithm
     When I request an Encoder for <ALGORITHM> from the Encoder Factory
     Then I expect the appropriate Encoder

    Examples:
      | ALGORITHM   |
      | HMAC_SHA1   |
      | HMAC_SHA256 |
      | HMAC_SHA512 |

  Scenario: invalid input
     When I request an Encoder for null
     Then I expect no Encoder to be returned
