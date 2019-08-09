package wangyonglin.utils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.math.BigInteger;
public class JavaKitBase64 {
    public static String hex(String hex) {
        BigInteger bi= new BigInteger(hex,16);
        return Base64.encode(bi);

    }
}
