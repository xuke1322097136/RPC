package framework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:58
 */
// 注意只有这个对象被序列化之后才能被发送
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Invocation implements Serializable {

    private String interfaceName;
    private String methodName;
    // 参数类型，因为方法有可能有多态
    private Class[] paramsTypes;
    // 具体的参数
    private Object[] params;
}
