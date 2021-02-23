package framework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by xuke
 * Description:
 * Date: 2021-01-25
 * Time: 0:45
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class URL implements Serializable {
    // url一般格式为：schema:hostname:port
    String hostname;
    int port;
}
