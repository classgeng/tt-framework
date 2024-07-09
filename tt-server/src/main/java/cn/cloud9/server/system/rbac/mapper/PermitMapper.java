package cn.cloud9.server.system.rbac.mapper;

import cn.cloud9.server.system.rbac.dto.PermitDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermitMapper extends BaseMapper<PermitDTO> {

    @Select({
        "SELECT ",
        "CONCAT(menu.menu_value, '@', permit.permit_value) AS permit ",
        "FROM sys_mepm AS mp ",
        "JOIN sys_menu AS menu ON mp.menu_id = menu.id ",
        "JOIN sys_permit AS permit ON mp.permit_id = permit.id ",
        "${ew.customSqlSegment}"
    })
    List<String> getPermitList(@Param(Constants.WRAPPER) QueryWrapper<PermitDTO> queryWrapper);
}
