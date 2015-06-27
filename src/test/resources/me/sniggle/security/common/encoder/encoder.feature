Feature: HmacEncoders
  Scenario Outline: d
    Given I have an encoder for <ALGORITHM> and the shared secret <SHARED_SECRET>
     When I encode <VALUE> using the encoder
     Then I expected this <RESULT> as result

    Examples:
      | ALGORITHM   | SHARED_SECRET | VALUE         | RESULT                                                                                                                           |
      | HMAC_SHA1   | testsecret    | wikipedia.org | 6c9adfe6565edafe191bc7bab5bc409bcd2b917d                                                                                         |
      | HMAC_SHA256 | testsecret    | wikipedia.org | 7c397b34447964ba44dd24ba4a8f5a37476e2a171ec0ebfb3419ac5618fede52                                                                 |
      | HMAC_SHA512 | testsecret    | wikipedia.org | c69a267f138953716a6c30d672084d91835268457a4a37a9de82cf7067d8af41d00cc069d9e2ac606a943990efae892e62f63b4fa985e7cf782e70d5cae99860 |


  Scenario Outline: null input
    Given I have an encoder for <ALGORITHM>
     When I try to encode a null value using the encoder
     Then I expect the encoder to return a null value

    Examples:
      | ALGORITHM   |
      | HMAC_SHA1   |
      | HMAC_SHA256 |
      | HMAC_SHA512 |