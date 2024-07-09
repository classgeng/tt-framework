package cn.cloud9.server.system.rbac.dto;

import cn.cloud9.server.struct.common.BaseDTO;
import cn.cloud9.server.struct.util.tree.ITreeNode;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description 菜单实体类
 * @project tt-server
 * @date 2022年12月06日 下午 11:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class MenuDTO extends BaseDTO<MenuDTO> implements ITreeNode<MenuDTO, Integer> {

    @TableField("app_code")
    private String appCode;

    @TableField("menu_name")
    private String menuName;

    @TableField("menu_value")
    private String menuValue;

    @TableField("menu_icon")
    private String menuIcon;

    @TableField("menu_level")
    private String menuLevel;

    /**
     * Function Directory
     */
    @TableField("menu_type")
    private String menuType;

    @TableField(exist = false)
    private String menuTypeName;

    @TableField("menu_sort")
    private String menuSort;

    @TableField("menu_route")
    private String menuRoute;

    @TableField("menu_path")
    private String menuPath;

    @TableField("is_show")
    private Boolean isShow;

    @TableField(exist = false)
    private String isShowName;

    @TableField("parent_id")
    private Integer parentId;

    @TableField(exist = false)
    private List<MenuDTO> children;

    @TableField(exist = false)
    private List<Integer> permitIds;

    @TableField(exist = false)
    private List<Integer> menuIds;

    @Override
    public List<MenuDTO> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<MenuDTO> children) {
        this.children = children;
    }


    private void setMenuTypeName() {
        boolean isEmpty = Objects.isNull(menuType);
        if (isEmpty) this.menuTypeName = "";
        else this.menuTypeName = "Directory".equals(menuType) ? "目录" : "菜单";
    }

    private void setIsShowName() {
        this.isShowName = Objects.nonNull(isShow) && isShow ? "是" : "否";
    }

    public void eachOne() {
        this.setMenuTypeName();
        this.setIsShowName();
    }
}
