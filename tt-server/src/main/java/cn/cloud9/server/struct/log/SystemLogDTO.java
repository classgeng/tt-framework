package cn.cloud9.server.struct.log;

import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.util.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 07:54
 */
@Data
@ToString
@TableName("sys_log")
public class SystemLogDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("module")
    private String module;

    @TableField("business_type")
    private String businessType;

    @TableField("method")
    private String method;

    @TableField("request_method")
    private String requestMethod;

    @TableField("url")
    private String url;

    @TableField("ip")
    private String ip;

    @TableField("address")
    private String address;

    @TableField("operate_system")
    private String operateSystem;

    @TableField("operator")
    private String operator;

    @TableField("param")
    private String param;

    @TableField("result")
    private String result;

    @TableField("status")
    private String status;

    @TableField("error_message")
    private String errorMessage;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField("create_time")
    private LocalDateTime createTime;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField("finish_time")
    private LocalDateTime finishTime;

    @TableField("duration")
    private Long duration;

    @TableField(exist = false)
    private Page<SystemLogDTO> page;
}
