package javakit.network;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
public class JavaKitClientResponse {
    public static void get(String url, JavaKitClientResponseCallback<String> resultCallback) {
        if(url==null&&url.equals("")){
            resultCallback.failure(new Exception("url not empty"));
        }
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {

            @Override
            public String call()  {
                // TODO Auto-generated method stub
                String result = null;
                Response response = null;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    resultCallback.failure(e);
                }

                if (response.isSuccessful()) {
                    try {
                        result=response.body().string();
                    } catch (IOException e) {
                        resultCallback.failure(e);
                    }
                }else {
                    resultCallback.failure(new Exception("network request error : " + response.code()));
                }
                return result;
            }});
        new Thread(task).start();

        try {

            resultCallback.success(task.get());

        } catch (InterruptedException e) {
            resultCallback.failure(e);
            return;
        } catch (ExecutionException e) {
            resultCallback.failure(e);
            return;
        }
    }
}
