import com.aliyun.IotCallback;
import com.aliyun.IotKit;
import org.junit.Test;

public class test implements IotCallback<String> {

    @Test
    public void _test()  {

    }


    @Override
    public void successful(String obj) {
        System.out.print(obj);
    }

    @Override
    public void failure(Exception e) {
        System.out.print(e.getMessage());
    }
}
