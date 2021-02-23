package framework;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by xuke
 * Description:
 * Date: 2021-02-23
 * Time: 22:43
 */
public class ProtocolFactory {

    public static Protocol getProtocol() {
        ServiceLoader<Protocol> serviceLoader = ServiceLoader.load(Protocol.class);
        Iterator<Protocol> iterator = serviceLoader.iterator();
        return iterator.next();
    }
}
