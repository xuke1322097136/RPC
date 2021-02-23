package protocol.http.consmuer;

import framework.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:55
 */
public class HttpClient {

    // 发送的数据，需要定义一个具体的数据，抽象成一个Invocation对象
    public String sendData(String hostname, int port, Invocation invocation) {
        try {
            // 这里的URL不是我们自己定义的URL
            URL url = new URL("http", hostname, port, "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            // 这里的post必须大写！
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            //最好采用try-with-resource来写
            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            // 对象通过输出流发送到provider的HttpServer绑定的地址和端口号，最后在HttpServerHandler里处理请求
            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            // 当provider将结果通过IOUtils工具返回回来的时候，这边可以拿到结果
            InputStream inputStream = httpURLConnection.getInputStream();
            String result = IOUtils.toString(inputStream);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
