package register;

import framework.URL;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:44
 */
public class RemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    // 导入我们自定义的URL
    public static void register(String interfaceName, URL url) {
       map.put(interfaceName, Collections.singletonList(url));
       saveFile();
    }

    public static URL random(String interfaceName) {
        map = getFile();
        List<URL> urls = map.get(interfaceName);
        Random random = new Random();
        int randomValue = random.nextInt(urls.size());
        return urls.get(randomValue);
    }

    private static void saveFile() {
        try(FileOutputStream outputStream = new FileOutputStream("shareFile.txt");
            ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            oos.writeObject(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<URL>> getFile() {
        try(FileInputStream inputStream = new FileInputStream("shareFile.txt");
            ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (Map<String, List<URL>>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
