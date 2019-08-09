package wangyonglin.aliyun;


import javakit.network.JavaKitClientResponse;
import javakit.network.JavaKitClientResponseCallback;

import javax.crypto.Mac;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class IotKit {
    static String accessKeyId = "";
    static String accessSecret = "";
    static  String ProductKey ="";
    static String Topic="";

    public static void Init(String ak,String as,String pk,String t){
        accessKeyId=ak;
        accessSecret=as;
        ProductKey=pk;
        Topic=t;
    }
        public static void Publish(String DeviceName, String Message,IotCallback callback){
            try {
                String url= UrlPublish("/"+ProductKey+"/"+DeviceName+"/"+Topic,Message);
                JavaKitClientResponse.get(url, new JavaKitClientResponseCallback<String>() {
                    @Override
                    public void success(String res) {
                        callback.successful(res);
                    }

                    @Override
                    public void failure(Exception e) {
                        callback.failure(e);
                    }
                });
            } catch (Exception e) {
                callback.failure(new Exception(e.getMessage()));
                e.printStackTrace();
            }
        }
        public static String UrlPublish(String TopicFullName, String Message) throws Exception {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区

            Map<String, String> paras = new HashMap<String, String>();
            // 1. 系统参数
            paras.put("SignatureMethod", "HMAC-SHA1");
            paras.put("SignatureNonce", java.util.UUID.randomUUID().toString());
            paras.put("AccessKeyId", accessKeyId);
            paras.put("SignatureVersion", "1.0");
            paras.put("Timestamp", df.format(new java.util.Date()));
            paras.put("Format", "JSON");

            // 2. 业务API参数
            paras.put("Action", "Pub");
            paras.put("Version", "2018-01-20");
            paras.put("RegionId", "cn-shanghai");
            paras.put("ProductKey", ProductKey);
            paras.put("TopicFullName", TopicFullName);
            paras.put("MessageContent",Message);
            paras.put("Qos","0");

            // 3. 去除签名关键字Key
            if (paras.containsKey("Signature"))
                paras.remove("Signature");

            // 4. 参数KEY排序
            TreeMap<String, String> sortParas = new TreeMap<String, String>();
            sortParas.putAll(paras);

            // 5. 构造待签名的字符串
            Iterator<String> it = sortParas.keySet().iterator();
            StringBuilder sortQueryStringTmp = new StringBuilder();
            while (it.hasNext()) {
                String key = it.next();
                sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(paras.get(key)));
            }
            String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号

            StringBuilder stringToSign = new StringBuilder();
            stringToSign.append("GET").append("&");
            stringToSign.append(specialUrlEncode("/")).append("&");
            stringToSign.append(specialUrlEncode(sortedQueryString));

            String sign = sign(accessSecret + "&", stringToSign.toString());
            // 6. 签名最后也要做特殊URL编码
            String signature = specialUrlEncode(sign);

            System.out.println(paras.get("SignatureNonce"));
            System.out.println("\r\n=========\r\n");
            System.out.println(paras.get("Timestamp"));
            System.out.println("\r\n=========\r\n");
            System.out.println(sortedQueryString);
            System.out.println("\r\n=========\r\n");
            System.out.println(stringToSign.toString());
            System.out.println("\r\n=========\r\n");
            System.out.println(sign);
            System.out.println("\r\n=========\r\n");
            System.out.println(signature);
            System.out.println("\r\n=========\r\n");
            // 最终打印出合法GET请求的URL
            return "https://iot.cn-shanghai.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp;
        }

        public static String specialUrlEncode(String value) throws Exception {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        }

        public static String sign(String accessSecret, String stringToSign) throws Exception {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return new sun.misc.BASE64Encoder().encode(signData);
        }


}
