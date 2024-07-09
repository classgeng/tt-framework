package cn.cloud9.server.system.common.area.dto;

import cn.cloud9.server.struct.util.tree.ITreeNode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author OnCloud9
 * @description 行政区域实体类
 * @project tt-server
 * @date 2022年11月19日 下午 07:59
 */
@Data
@TableName("region2022")
public class AreaDTO implements ITreeNode<AreaDTO, Long> {

    /**
     * 区域主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 区域名称
     */
    @TableField("area_name")
    private String areaName;

    /**
     * 区域上级主键
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 层级
     */
    @TableField("area_level")
    private Integer areaLevel;

    /**
     * 路径
     */
    @TableField("area_path")
    private String areaPath;

    /**
     * 连接地址
     */
    @TableField("area_url")
    private String areaUrl;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    List<AreaDTO> children;

    @TableField(exist = false)
    private Page<AreaDTO> page;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getParentId() {
        return this.parentId;
    }

    @Override
    public List<AreaDTO> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<AreaDTO> children) {
        this.children = children;
    }
}
