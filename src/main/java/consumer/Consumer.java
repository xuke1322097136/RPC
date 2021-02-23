package consumer;

import framework.Invocation;
import protocol.http.consmuer.HttpClient;
import provider.api.HelloService;
import proxy.ProxyFactory;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:53
 */
public class Consumer {
    public static void main(String[] args) {
        // 方案1：直接在Consumer传输数据
//        HttpClient httpClient = new HttpClient();
//        Invocation invocation = new Invocation(HelloService.class.getName(), "sayHello", new Class[]{String
//        .class}, new Object[]{"xuke"});
//        String result = httpClient.sendData("localhost", 8080, invocation);
//        System.out.println(result);

        // 方案2：使用动态代理，具体数据传输逻辑方在代理对象中，并且Invocation中的参数可以直接由invoke中的参数决定
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        // 拿到代理对象之后，调用目标方法的时候，就会调用代理对象的invoke方法，method参数就是sayHello，args就是"xuke"
        String result = helloService.sayHello(" xuke");
        System.out.println(result);
    }
}
