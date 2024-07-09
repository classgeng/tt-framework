package cn.cloud9.server.struct.datauth.config;

import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.datauth.DataAuthIdent;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.dto.map.RoUsDTO;
import cn.cloud9.server.system.rbac.mapper.MenuMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoMeMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoUsMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 下午 02:52
 */
@Service("dataAuthConfigService")
public class DataAuthConfigServiceImpl extends ServiceImpl<DataAuthConfigMapper, DataAuthConfigDTO> implements IDataAuthConfigService {

    @Resource
    private RoUsMapper roUsMapper;

    @Resource
    private RoMeMapper roMeMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<DataAuthConfigDTO> getAssignableList(Integer userId) {
        /* 1、获取角色Id集合 */
        final List<RoUsDTO> roUsList = roUsMapper.selectList(Wrappers.<RoUsDTO>lambdaQuery().eq(RoUsDTO::getUserId, userId));
        if (CollectionUtils.isEmpty(roUsList)) return Collections.emptyList();
        final List<Integer> roleIds = roUsList.stream().map(RoUsDTO::getRoleId).collect(Collectors.toList());

        /* 2、获取菜单集合 */
        final List<RoMeDTO> roMeList = roMeMapper.selectList(Wrappers.<RoMeDTO>lambdaQuery().in(RoMeDTO::getRoleId, roleIds).eq(RoMeDTO::getMenuType, Constant.ROME_TYPE_MENU));
        if (CollectionUtils.isEmpty(roMeList)) return Collections.emptyList();
        final List<Integer> menuIds = roMeList.stream().map(RoMeDTO::getMenuId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(menuIds)) return Collections.emptyList();
        final List<MenuDTO> menuList = menuMapper.selectList(Wrappers.<MenuDTO>lambdaQuery().in(MenuDTO::getId, menuIds));

        /* 3、查找授权配置项 */
        final List<DataAuthIdent> daIdents = new ArrayList<>();
        for (MenuDTO menu : menuList) {
            final Optional<DataAuthIdent> daIdentOpt = Arrays.stream(DataAuthIdent.values()).filter(daIdent -> daIdent.getIdentKey().equals(menu.getMenuValue())).findFirst();
            daIdentOpt.ifPresent(daIdents::add);
        }
        if (CollectionUtils.isEmpty(daIdents)) return Collections.emptyList();

        /* 4、按identKey查询所有配置项 */
        final List<String> identKeys = daIdents.stream().map(DataAuthIdent::getIdentKey).collect(Collectors.toList());
        return lambdaQuery().in(DataAuthConfigDTO::getIdentKey, identKeys).list();
    }
}
