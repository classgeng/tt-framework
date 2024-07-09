package cn.cloud9.server.struct.authority.jwt.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 12:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken  implements Serializable {

    private static final long serialVersionUID = -8482946147572784305L;

    /**
     * token
     */
    private String token;

    /**
     * 有效时间：单位：秒
     */
    private Integer expire;
}
