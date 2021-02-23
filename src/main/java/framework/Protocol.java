package framework;

/**
 * Created by xuke
 * Description: 协议扩展机制，具体的Http协议和dubbo协议要实现该接口中的方法
 * Date: 2021-02-22
 * Time: 0:47
 */
public interface Protocol {
    // HttpServer/NettyServer端的方法
    void start(URL url);
    // HttpClient/NettyClient端的方法
    String sendData(URL url, Invocation invocation);
}
