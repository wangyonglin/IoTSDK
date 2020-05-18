

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.junit.Test;
import wangyonglin.aliyun.IotCallback;
import wangyonglin.aliyun.IotKit;
import wangyonglin.utils.JavaKitBase64;

import java.math.BigInteger;


public class test implements IotCallback<String> {
    private static String accessKeyId = "";
    private static String accessSecret = "";
    private static  String ProductKey ="";
    private static String Topic="user/router";
    @Test
    public void _test()  {
        IotKit.Init(accessKeyId,accessSecret,ProductKey,Topic);
        IotKit.Publish("ESP82660003", JavaKitBase64.hex("FD03CB2A8854DF"),this);


    }





    @Override
    public void successful(String obj) {
        System.out.print(obj);
    }

    @Override
    public void failure(Exception e) {
        e.printStackTrace();
    }
}
