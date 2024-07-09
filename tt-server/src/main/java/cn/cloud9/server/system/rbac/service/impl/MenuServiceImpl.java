package cn.cloud9.server.system.rbac.service.impl;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.mapper.MenuMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoMeMapper;
import cn.cloud9.server.system.rbac.service.IMenuService;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cloud9.server.struct.constant.Constant.ROME_TYPE_MENU;
import static cn.cloud9.server.struct.constant.Constant.ROME_TYPE_PERMIT;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 上午 12:14
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDTO> implements IMenuService {

    @Resource
    private RoMeMapper roMeMapper;

    @Override
    public IPage<MenuDTO> queryMenuPage(MenuDTO dto) {
        final Page<MenuDTO> page = lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getAppCode()), MenuDTO::getAppCode, dto.getAppCode())
                .like(StringUtils.isNotBlank(dto.getMenuName()), MenuDTO::getMenuName, dto.getMenuName())
                .like(StringUtils.isNotBlank(dto.getMenuValue()), MenuDTO::getMenuValue, dto.getMenuValue())
                .eq(StringUtils.isNotBlank(dto.getMenuType()), MenuDTO::getMenuType, dto.getMenuType())
                .eq(StringUtils.isNotBlank(dto.getMenuLevel()), MenuDTO::getMenuLevel, dto.getMenuLevel())
                .eq(Objects.nonNull(dto.getIsShow()), MenuDTO::getIsShow, dto.getIsShow())
                .between(StringUtils.isNotBlank(dto.getStartCreateTime()) && StringUtils.isNotBlank(dto.getEndCreateTime()),
                        MenuDTO::getCreateTime,
                        dto.getStartCreateTime(),
                        dto.getEndCreateTime())
                .orderByDesc(MenuDTO::getCreateTime)
                .page(dto.getPage());

        page.getRecords().forEach(MenuDTO::eachOne);
        return page;
    }

    @Override
    public List<MenuDTO> getMenuList(UserContext userContext) {
        final Long userId = userContext.getUserId();
        QueryWrapper<MenuDTO> wrapper = Wrappers.<MenuDTO>query()
                .eq("ru.user_id", userId)
                .orderByAsc("menu.menu_sort");
        return baseMapper.getMenuList(wrapper);
    }

    @Override
    public List<MenuDTO> getCheckedMenuList(Integer sysRoleId) {
        /* 按角色ID关联，类型为菜单的 */
        final Wrapper<RoMeDTO> query = Wrappers.<RoMeDTO>lambdaQuery()
                .eq(RoMeDTO::getRoleId, sysRoleId)
                .eq(RoMeDTO::getMenuType, ROME_TYPE_MENU);
        final List<RoMeDTO> roMeList = roMeMapper.selectList(query);
        if(CollectionUtils.isEmpty(roMeList)) return Collections.emptyList();
        final List<Integer> menuIds = roMeList.stream().map(RoMeDTO::getMenuId).collect(Collectors.toList());
        return lambdaQuery().in(MenuDTO::getId, menuIds).list();
    }

    @Override
    public void assignMenus(RoleDTO role) {
        final Integer roleId = role.getId();
        final List<Integer> menuIds = role.getMenuIds();
        /* 该角色授予的菜单下已经授予了对应权限，则不能重新授予，需要先清除权限的授予 */
        final Wrapper<RoMeDTO> query = Wrappers.<RoMeDTO>lambdaQuery()
                .eq(RoMeDTO::getRoleId, roleId)
                .eq(RoMeDTO::getMenuType, ROME_TYPE_PERMIT);
        final List<RoMeDTO> assignedPermits = roMeMapper.selectList(query);
        Assert.isTrue(CollectionUtils.isNotEmpty(assignedPermits), ResultMessage.SUBMIT_FAIL, "请先重置已授予的权限！");

        roMeMapper.delete(Wrappers.<RoMeDTO>lambdaQuery().eq(RoMeDTO::getRoleId, roleId));


        final String account = UserContextHolder.getUserContext().getUserAccount();
        final LocalDateTime now = LocalDateTime.now();
        menuIds.forEach(menuId -> {
            final RoMeDTO roMe = new RoMeDTO();
            roMe.setRoleId(roleId);
            roMe.setMenuId(menuId);
            roMe.setMenuType(ROME_TYPE_MENU);
            roMe.setCreator(account);
            roMe.setCreateTime(now);
            roMeMapper.insert(roMe);
        });
    }
}
