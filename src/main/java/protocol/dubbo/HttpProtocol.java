package protocol.dubbo;

import framework.Invocation;
import framework.Protocol;
import framework.URL;
import protocol.http.consmuer.HttpClient;
import protocol.http.provider.HttpServer;

/**
 * Created by xuke
 * Description:
 * Date: 2021-02-22
 * Time: 0:55
 */
public class HttpProtocol implements Protocol {
    @Override
    public void start(URL url) {
        new HttpServer().start(url.getHostname(), url.getPort());
    }

    @Override
    public String sendData(URL url, Invocation invocation) {
        return new HttpClient().sendData(url.getHostname(), url.getPort(), invocation);
    }
}
