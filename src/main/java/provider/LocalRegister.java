package provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:36
 */
public class LocalRegister {

    private static Map<String, Class> map = new HashMap<>();

    public static void register(String interfaceName, Class implClass) {
        map.put(interfaceName, implClass);
    }

    public static Class get(String interfaceName) {
        return map.get(interfaceName);
    }













}
