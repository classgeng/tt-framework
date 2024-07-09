package cn.cloud9.server.test.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月12日 下午 09:11
 */
@Data
public class RedisModel implements Serializable {
    private boolean status;
    private Integer amount;
    private String code;
    private LocalDateTime createTime;
    private LocalDate updateDate;
    private LocalTime timeLine;
    private Date timeline2;
}
