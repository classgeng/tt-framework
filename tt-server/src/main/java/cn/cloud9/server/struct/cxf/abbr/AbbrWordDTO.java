package cn.cloud9.server.struct.cxf.abbr;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月28日 下午 09:41
 */
@XmlRootElement(name = "AbbrWordDTO")
@Data
@TableName("abridge_word")
public class AbbrWordDTO implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    @TableField("NAME")
    private String name;
    @TableField("FULL_NAME")
    private String fullName;
    @TableField("MEANING")
    private String meaning;

    /* CXF不支持LocalDateTime初始化构造？？？ */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("GEN_TIME")
    private Date genTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("ALT_TIME")
    private Date altTime;

    @TableField(exist = false)
    Page<AbbrWordDTO> page;
}
