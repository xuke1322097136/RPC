package provider.api.impl;

import provider.api.HelloService;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:04
 */
public class HelloServiceImpl implements HelloService {

    public String sayHello(String username) {
        return "hello" + username;
    }
}
