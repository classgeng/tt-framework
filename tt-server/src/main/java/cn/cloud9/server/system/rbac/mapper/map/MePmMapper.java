package cn.cloud9.server.system.rbac.mapper.map;

import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MePmMapper extends BaseMapper<MePmDTO> {

    @Select({
        "SELECT ",
        " menu.menu_name AS menuName, ",
        " menu.menu_value AS menuValue, ",
        " permit.permit_name AS permitName, ",
        " permit.permit_value AS permitValue, ",
        " mp.* ",
        "FROM sys_mepm AS mp ",
        " JOIN sys_menu AS menu ON mp.menu_id = menu.id ",
        " JOIN sys_permit AS permit ON mp.permit_id = permit.id ",
        "${ew.customSqlSegment}"
    })
    List<MePmDTO> getAllocatedPermitList(@Param(Constants.WRAPPER) Wrapper<MePmDTO> wrapper);
}
