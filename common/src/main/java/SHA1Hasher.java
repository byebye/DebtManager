/**
 * Created by glapul on 10.05.15.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SHA1Hasher {

    public static String hash(String original)  {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        }
        catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("That shall not happen");
        }
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
}