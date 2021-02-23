package proxy;

import framework.Invocation;
import framework.Protocol;
import framework.ProtocolFactory;
import framework.URL;
import protocol.dubbo.HttpProtocol;
import protocol.http.consmuer.HttpClient;
import provider.api.HelloService;
import register.RemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xuke
 * Description:
 * Date: 2021-02-22
 * Time: 0:04
 */
public class ProxyFactory {

    public static <T> T getProxy(Class interfaceName) {
        // 利用jdk动态代理来实现
        return (T) Proxy.newProxyInstance(interfaceName.getClassLoader(), new Class[]{interfaceName}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 协议扩展机制，将HttpClient改成Protocol，使用Protocol代替
                // 原来不使用协议扩展：HttpClient httpClient = new HttpClient();然后下面调用sendData也是httpClient
                // 使用协议扩展：使用HttpProtocol代替，然后sendData也用HttpProtocol代替
                // Protocol protocol = new HttpProtocol();
                // 下面使用java SPI机制来实现的话就是：
                Protocol protocol = ProtocolFactory.getProtocol();
                Invocation invocation = new Invocation(interfaceName.getName(), method.getName(), method.getParameterTypes(), args);

                // 方案1：地址和端口直接写死的形式
               // return httpClient.sendData("localhost", 8080, invocation);

                // 方案2：通过注册中心的负载均衡机制拿到目标地址和端口
                URL url = RemoteRegister.random(interfaceName.getName());
                // 不适用协议扩展：return httpClient.sendData(url.getHostname(), url.getPort(), invocation);
                return protocol.sendData(url, invocation);

            }
        });
    }
}
