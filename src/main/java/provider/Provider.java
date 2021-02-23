package provider;

import framework.Protocol;
import framework.ProtocolFactory;
import framework.URL;
import protocol.dubbo.HttpProtocol;
import protocol.http.provider.HttpServer;
import provider.api.HelloService;
import provider.api.impl.HelloServiceImpl;
import register.RemoteRegister;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:06
 */
public class Provider {
    // 生产者的启动类
    public static void main(String[] args) {
        // 1. 本地注册，存暴露出去的服务名字和实现类
        LocalRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        // 2. 远程注册，<服务名， List<URL>>，也就是说这个服务有多台机器都有这个服务，集群
        URL url = new URL("localhost", 8080);
        RemoteRegister.register(HelloService.class.getName(), url);

        // 方案1：3. 启动Tomcat，暴漏服务
//        HttpServer httpServer = new HttpServer();
//        httpServer.start("localhost", 8080);
        // 方案2：使用协议的扩展机制
        // Protocol protocol = new HttpProtocol();
        // 方案3： 使用java SPI机制来实现
        Protocol protocol = ProtocolFactory.getProtocol();
        protocol.start(url);
    }
}
