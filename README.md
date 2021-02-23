基于Tomcat的Http协议手动实现一个RPC框架，参考视频：https://www.bilibili.com/video/BV1e4411F75R?p=4&spm_id_from=pageDriver

1. 首先服务提供方先要有服务（HelloService）及其实现类(HelloServiceImpl);
2. 服务提供方要有一个启动类Provider，然后里面要有3小步：
   1.)本地暴露服务，详见LocalRegister即可
   2.)远程暴露服务，详见RemoteRegister，里面需要绑定我们自定义的URL（其实就是服务提供方的地址和端口号）。
   3.)启动Tomcat暴露服务，其实就是实现一个HttpServer，里面定义一个start方法，此时pom.xml需要引入内嵌的tomcat，至于怎么
   使用tomcat，可以参照tomcat结构这张图。最后tomcat是一个servlet容器，必须指定servlet来处理请求，所以建了一个DispatcherServlet，
   与此同时新建一个HttpServerHandler来处理请求并返回结果。
   
3. 服务调用方也必须要有一个启动类Consumer：
   1.)类似的，它也要有一个HttpClient，里面有一个sendData方法用于发送数据，里面包含你要发送的地址和端口，以及你要发送的数据Invocation，
      注意，Invocation是需要通过http协议网络传输的，所以需要实现Serializable接口。
      
4. 服务提供方通过DispatcherServlet绑定的HttpServerHandler拿到对应的数据，本地注册中解析出服务名、方法名，然后反射调用本地方法，
     并将结果回写到输出流中，返回给服务使用者即consumer。
     
5. consumer端的httpclient接收到provider返回回来的结果之后打印出来即可。此时启动Provider类和Consumer类就可以跑起来整个demo。

一步一步演变：
演变一：
另外一点，在dubbo官方源码里的demo中，在consumer中有下面代码：
             DemoService demoService = context.getBean("demoService", DemoService.class);
             String hello = demoService.sayHello("world);
第一行的意思其实是从Spring容器中拿到具体实现类的代理对象，因为comsumer没有具体的实现类的，具体的实现类在provider端，
然后在consumer端调用目标方法。所以，我们的Consumer的版本一可以改进为版本二，具体的数据传输放到代理类中的代理方法去实现，详见ProxyFactory的实现。
演变二：consumer调用的时候传了地址和端口，其实consumer调用某个服务的时候并不想关心地址和端口。地址和端口是从注册中心RemoteRegister查找出来的，
        然后再进行负载均衡就可以了。负载均衡可以参见RemoteRegister简单的随机算法，当然其实还有很多种具体的实现形式。
         consumer原始写死的调用方式：httpClient.sendData("localhost", 8080, invocation);
         consumer改进之后的写法：URL url = RemoteRegister.random(interfaceName.getName());
                                httpClient.sendData(url.getHostname(), url.getPort(), invocation);

这个时候分别启动Provider和Consumer的时候，会报NPE，为啥呢？Provider不是把list里的URL注册到注册中心了吗？为什么Consumer会拿不到呢？
原因其实很简单，因为Provider和Consumer是两个不同的进程，你把URL注册到了Provider进程里的map里，但是Consumer中map的list是空的！
其实因为两个进程位于同一台机器，所以可以用共享来解决这个问题，详见RemoteRegister。
至此，我们http协议就写完了，当然protocol里面还可以定义dubbo协议，里面也是类似的，由NettyServer、NettyClient等。我们可以修改源码里的HttpServer、HttpClient来调用即可。
但是，这种方式明显是不妥的，要是每来一个协议就改一次，肯定是不行的，所以这里用到了dubbo的SPI扩展机制。

自定义一个Protocol接口，里面包含服务端和客户端要用到的方法，然后DubboProtocol和HttpProtocol分别实现里面的方法，
最后在Consumer和Provider中使用的时候，使用对应的协议即可。详见Consumer的代理类ProxyFactory和Provider，但是这里还是会修改代码，
所以咱们可以使用java SPI机制来完成，修改成下面形式就可以了：
                               Protocol protocol = ProtocolFactory.getProtocol();
里面要加载到我们定义的实现类，需要在resources目录下新建一个META-INF文件夹，然后再建一个services文件夹，再建一个接口全限定名的类framework.Protocol的文件，
然后在文件里面写上具体的实现类。
JAVA SPI其实在数据库驱动也能看得到身影：mysql驱动和下面的sqlite驱动主要是用来观察JAVA SPI用的，因为可以看到在对应的jar包的
META-INF/services下面有个java.sql.Driver，同理，sqlite-jdbc也是一样的，也有一个java.sql.Driver，然后该文件里面的内容就是不同驱动具体的实现类。
所以我们想要用哪种数据库驱动，我们只要引入对应的jar包即可，jar下面的META-INF/services/java.sql.Driver里面就有对应的数据库驱动实现类。                               
dubbo SPI的好处：1.）java spi每次只能拿到其中一个实现类，而dubbo spi可以通过key-value的形式拿到指定的实现类；
                 2.）依赖注入：也就是说我们在 META-INF/services下的文件里写的实现类要是以来别的类的话，那么它会帮助我们将依赖的类一并注入进来；
                 3.）AOP：如果想在实现类的前面和后面做一些操作的话，dubbo spi也是支持的；
                 4.）效率和性能比JAVA SPI要高。                
                               
                               
                               
                               