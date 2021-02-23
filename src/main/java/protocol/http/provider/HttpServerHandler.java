package protocol.http.provider;

import framework.Invocation;
import org.apache.commons.io.IOUtils;
import org.omg.PortableInterceptor.INACTIVE;
import provider.LocalRegister;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:31
 */
public class HttpServerHandler {
    // 接收到consumer的请求，处理请求并返回结果
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            InputStream inputStream = req.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            // 反序列化请求
            Invocation invocation = (Invocation)ois.readObject();

            // 从本地注册中拿到实现类（服务名），找到实现类里面的方法，然后调用本地方法
            Class implClass = LocalRegister.get(invocation.getInterfaceName());
            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamsTypes());
            String result = (String) method.invoke(implClass.newInstance(), invocation.getParams());

            // 将结果写回到consumer
            IOUtils.write(result, resp.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
