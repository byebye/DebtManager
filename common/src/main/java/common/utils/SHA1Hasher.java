package common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Hasher {

  public static String hash(String original) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA1");
    }
    catch (NoSuchAlgorithmException e) {
      assert false : "That shall not happen";
    }
    md.update(original.getBytes());
    byte[] digest = md.digest();
    StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }
}