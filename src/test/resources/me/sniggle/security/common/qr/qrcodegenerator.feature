Feature: QR Code Generator
  Scenario Outline: Generate QR Code successfully
    Given I have the reference image <REFERENCE_FILE>
      And I have an instance of the QRCodeGenerator
      And I have the following parameters
        | TYPE   | ISSUER   | ACCOUNT   | BASE32_SECRET   |
        | <TYPE> | <ISSUER> | <ACCOUNT> | <BASE32_SECRET> |
     When I generate the QR Code Image
     Then I expect the generated image to be identical to the reference image

    Examples:
      | REFERENCE_FILE  | TYPE | ISSUER | ACCOUNT      | BASE32_SECRET                    |
      | qrcode-test.png | TOTP | Issuer | Account Name | 2PLNQZGC46MGL5XRSYV4J2JUXXVFGHE4 |