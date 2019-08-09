package wangyonglin.utils;
import javakit.apache.commons.codec.binary.Base64;
import java.math.BigInteger;
public class JavaKitBase64 {
    public static String hex(String hex) {
        BigInteger bi= new BigInteger(hex,16);
        return Base64.encodeBase64String(bi.toByteArray());

    }
}
