package protocol.dubbo;

import framework.Invocation;
import framework.Protocol;
import framework.URL;
import org.apache.tomcat.util.buf.UriUtil;

/**
 * Created by xuke
 * Description:
 * Date: 2021-02-22
 * Time: 0:52
 */
public class DubboProtocol implements Protocol {
    @Override
    public void start(URL url) {
     //        new NettyServer().start(url.getHostName(), url.getPort())
    }

    @Override
    public String sendData(URL url, Invocation invocation) {
     //        return new NettyClient().sendData(url.getHostname(), url.getPort(), invocation);
        return null;
    }
}
