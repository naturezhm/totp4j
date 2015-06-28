package me.sniggle.security.common.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import me.sniggle.security.common.Parameter;
import me.sniggle.security.common.TwoFactorTypes;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iulius on 28/06/15.
 */
public class QRCodeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeGenerator.class);

  private static final String SCHEME = "otpauth";

  /**
   *
   * @param type
   * @param issuer
   * @param accountName
   * @param secret
   * @param parameters
   * @return
   * @throws UnsupportedEncodingException
   */
  private String buildUrl(TwoFactorTypes type, String issuer, String accountName, String secret, Map<Parameter, String> parameters) throws UnsupportedEncodingException {
    LOGGER.trace("buildUrl(TwoFactorTypes, String, String, String, Map<Parameter, String>)");
    String result = null;
    StringBuilder parameterString = new StringBuilder();
    for( Map.Entry<Parameter, String> entry : parameters.entrySet() ) {
      parameterString.append(entry.getKey().paramName());
      parameterString.append('=');
      parameterString.append(entry.getValue());
      parameterString.append('&');
    }
    parameterString.setLength(parameterString.length()-1);
    try {
      URI uri = new URI(SCHEME, type.toString().toLowerCase(), "/"+issuer+":"+accountName, parameterString.toString(), null);
      result = uri.toString();
    }catch(Exception e) {
      LOGGER.error("Error: {}", e.getMessage());
    }
    return result;
  }

  /**
   *
   * @param type
   * @param parameters
   * @return
   */
  private boolean validateParameters(TwoFactorTypes type, Map<Parameter, String> parameters) {
    LOGGER.trace("validateParameters(TwoFactorTypes, Map<Parameter, String>)");
    boolean result = true;
    for( Parameter param : Parameter.values() ) {
      //Check if mandatory parameters are present
      if( type == TwoFactorTypes.HOTP ) {
        if( param.isMandatoryHOTP() && parameters.get(param) == null) {
          LOGGER.error("Mandatory parameter '{}' for type HOTP missing!", param);
          result &= false;
        }
      } else if( type == TwoFactorTypes.TOTP ) {
        if( param.isMandatoryTOTP() && parameters.get(param) == null ) {
          LOGGER.error("Mandatory parameter '{}' for type TOTP missing!", param);
          result &= false;
        }
      }
      //Check if parameters are in allowed set
      if( param.allowedValues() != null && parameters.get(param) != null) {
        String value = parameters.get(param);
        boolean matches = false;
        for( String allowedValue : param.allowedValues() ) {
          if( allowedValue.equals(value) ) {
            matches = true;
          }
        }
        if( !matches ) {
          LOGGER.error("The value ({}) of parameter {} is not an allowed value ({})", value, param, param.allowedValues());
        }
        result &= matches;
      }
    }
    LOGGER.debug("Parameter validation {}", result ? "successful" : "failed");
    return result;
  }

  /**
   *
   * @param parameters
   * @param secret
   * @param issuer
   * @return
   */
  private Map<Parameter, String> prepareParameters(Map<Parameter, String> parameters, String secret, String issuer) {
    LOGGER.trace("prepareParameters(Map<Parameter, String>, String, String)");
    Map<Parameter, String> copyParameters = new HashMap<>(parameters);
    copyParameters.put(Parameter.SECRET, secret);
    copyParameters.put(Parameter.ISSUER, issuer);
    return copyParameters;
  }

  /**
   *
   * @return
   */
  private Map<EncodeHintType, Object> prepareEncodeHint() {
    LOGGER.trace("prepareEncodeHint()");
    Map<EncodeHintType, Object> encodingHints = new HashMap<>();
    encodingHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    return encodingHints;
  }

  /**
   *
   * @param width
   * @param height
   * @param bitMatrix
   * @return
   */
  private RenderedImage renderQRCode(int width, int height, BitMatrix bitMatrix) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (bitMatrix.get(x, y)) {
          graphics.fillRect(x, y, 1, 1);
        }
      }
    }
    return image;
  }

  /**
   *
   * @param type
   * @param issuer
   * @param accountName
   * @param secret
   * @param parameters
   * @return
   */
  public byte[] generateQRCode(TwoFactorTypes type, String issuer, String accountName, byte[] secret, Map<Parameter, String> parameters) {
    LOGGER.trace("generateQRCode(TwoFactorTypes, String, String, byte[], Map<Parameter, String>)");
    return generateQRCode(type, issuer, accountName, new Base32().encodeAsString(secret), parameters);
  }

  /**
   *
   * @param type
   * @param issuer
   * @param accountName
   * @param secret
   * @param parameters
   * @return
   */
  public byte[] generateQRCode(TwoFactorTypes type, String issuer, String accountName, String secret, Map<Parameter, String> parameters) {
    LOGGER.trace("generateQRCode(TwoFactorTypes, String, String, String, Map<Parameter, String>)");
    ByteArrayOutputStream result = null;
    Map<Parameter, String> preparedParameters = prepareParameters(parameters, secret, issuer);
    if( validateParameters(type, preparedParameters) ) {
      int height = 512, width = 512;
      try {
        result = new ByteArrayOutputStream();
        QRCodeWriter writer = new QRCodeWriter();
        String url = buildUrl(type, issuer, accountName, secret, preparedParameters);
        BitMatrix qrCodeMatrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height, prepareEncodeHint());
        ImageIO.write(renderQRCode(width, height, qrCodeMatrix), "PNG", result);
      } catch(IOException | WriterException e) {
        LOGGER.error("Error: {}", e.getMessage());
        result = null;
      }
    }
    return (result == null)? null : result.toByteArray();
  }

}
