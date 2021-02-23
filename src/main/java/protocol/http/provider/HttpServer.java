package protocol.http.provider;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:11
 */
public class HttpServer {

    public void start(String hostname, int port) {
        Tomcat tomcat = new Tomcat();

        // 参考tomcat结构一层一层地添加
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostname);

        Host standardHost = new StandardHost();
        standardHost.setName(hostname);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        standardHost.addChild(context);
        engine.addChild(standardHost);

        service.setContainer(engine);
        service.addConnector(connector);

        // tomcat 是一个servlet容器，所以必须要有对应的servlet来处理请求，并指定映射关系，所有的请求都会经过名字为dispatcher的servlet
        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
