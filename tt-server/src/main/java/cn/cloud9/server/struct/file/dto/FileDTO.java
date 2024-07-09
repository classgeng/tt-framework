package cn.cloud9.server.struct.file.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author OnCloud9
 * @description 文件上传记录实体
 * @project tt-server
 * @date 2022年11月15日 下午 11:54
 */
@Data
@TableName("file")
public class FileDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("SRC_FILENAME")
    private String srcFilename;

    @TableField("SPEC_FILENAME")
    private String specFilename;

    @TableField("SUB_DIRECTORY")
    private String subDirectory;

    @TableField("CONTENT_TYPE")
    private String contentType;

    @TableField("FILE_SIZE")
    private Long fileSize;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("CREATOR")
    private String creator;
}
