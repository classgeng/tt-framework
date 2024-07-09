package cn.cloud9.server.system.rbac.mapper;

import cn.cloud9.server.system.rbac.dto.MenuDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<MenuDTO> {

    /**
     * 查询菜单集合
     * @param queryWrapper 查询条件对象
     * @return List<MenuDTO> 菜单集合
     */
    @Select({
        "SELECT menu.* ",
        "FROM sys_menu AS menu ",
        "JOIN sys_rome AS rm ON rm.menu_id = menu.id AND rm.menu_type = 'menu' ",
        "JOIN sys_rous AS ru ON rm.role_id = ru.role_id ",
        "${ew.customSqlSegment}"
    })
    List<MenuDTO> getMenuList(@Param(Constants.WRAPPER) QueryWrapper<MenuDTO> queryWrapper);
}
