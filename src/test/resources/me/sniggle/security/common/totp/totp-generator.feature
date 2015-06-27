Feature: TOTP Password Generator
  Scenario Outline: Generating TOTP tokens with defaults
    Given I have an instance of the TOTP Generator instantiated
      And I have initialized this TOTP Generator with default parameters
      And I the unix timestamp used is <TIMESTAMP>
     When I calculate the TOTP Code
     Then I expect the following <TOTP_CODE>

    Examples:
      | TOTP_CODE | TIMESTAMP   |
      | 287082    | 59          |
      | 081804    | 1111111109  |
      | 050471    | 1111111111  |
      | 005924    | 1234567890  |
      | 279037    | 2000000000  |
      | 353130    | 20000000000 |

  Scenario Outline: Generating TOTP tokens with non-default values
    Given I have an instance of the TOTP Generator instantiated
      And I have initialized this TOTP Generator with the following parameters
          | EPOCH   | INTERVAL   | ALGORITHM   | TOKEN_LENGTH   |
          | <EPOCH> | <INTERVAL> | <ALGORITHM> | <TOKEN_LENGTH> |
      And I the unix timestamp used is <TIMESTAMP>
    When I calculate the TOTP Code
    Then I expect the following <TOTP_CODE>

    Examples:
      | EPOCH | INTERVAL | ALGORITHM | TOKEN_LENGTH | TIMESTAMP   | TOTP_CODE |
      | 0     | 30       | SHA1      | 8            | 59          | 94287082  |
      | 0     | 30       | SHA256    | 8            | 59          | 46119246  |
      | 0     | 30       | SHA512    | 8            | 59          | 90693936  |
      | 0     | 30       | SHA1      | 8            | 1111111109  | 07081804  |
      | 0     | 30       | SHA256    | 8            | 1111111109  | 68084774  |
      | 0     | 30       | SHA512    | 8            | 1111111109  | 25091201  |
      | 0     | 30       | SHA1      | 8            | 1111111111  | 14050471  |
      | 0     | 30       | SHA256    | 8            | 1111111111  | 67062674  |
      | 0     | 30       | SHA512    | 8            | 1111111111  | 99943326  |
      | 0     | 30       | SHA1      | 8            | 1234567890  | 89005924  |
      | 0     | 30       | SHA256    | 8            | 1234567890  | 91819424  |
      | 0     | 30       | SHA512    | 8            | 1234567890  | 93441116  |
      | 0     | 30       | SHA1      | 8            | 2000000000  | 69279037  |
      | 0     | 30       | SHA256    | 8            | 2000000000  | 90698825  |
      | 0     | 30       | SHA512    | 8            | 2000000000  | 38618901  |
      | 0     | 30       | SHA1      | 8            | 20000000000 | 65353130  |
      | 0     | 30       | SHA256    | 8            | 20000000000 | 77737706  |
      | 0     | 30       | SHA512    | 8            | 20000000000 | 47863826  |
