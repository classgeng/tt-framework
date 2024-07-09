package cn.cloud9.server.system.rbac.service.impl;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.lambda.ObjectBuilder;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.PermitDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.mapper.PermitMapper;
import cn.cloud9.server.system.rbac.mapper.map.MePmMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoMeMapper;
import cn.cloud9.server.system.rbac.service.IPermitService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cloud9.server.struct.constant.Constant.ROME_TYPE_MENU;
import static cn.cloud9.server.struct.constant.Constant.ROME_TYPE_PERMIT;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 上午 12:15
 */
@Slf4j
@Service("permitService")
public class PermitServiceImpl extends ServiceImpl<PermitMapper, PermitDTO> implements IPermitService {

    @Resource
    private RoMeMapper roMeMapper;

    @Resource
    private MePmMapper mePmMapper;

    /**
     * 权限翻页查询
     * @param dto 权限实体
     * @return IPage<PermitDTO>
     */
    @Override
    public IPage<PermitDTO> queryPermitPage(PermitDTO dto) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getPermitName()), PermitDTO::getPermitName, dto.getPermitName())
                .like(StringUtils.isNotBlank(dto.getPermitValue()), PermitDTO::getPermitValue, dto.getPermitValue())
                .between(StringUtils.isNotBlank(dto.getStartCreateTime()) && StringUtils.isNotBlank(dto.getEndCreateTime()),
                        PermitDTO::getCreateTime,
                        dto.getStartCreateTime(),
                        dto.getEndCreateTime())
                .orderByDesc(PermitDTO::getCreateTime)
                .page(dto.getPage());
    }

    /**
     * 获取当前用户的权限集合
     * @param userContext 用户信息
     * @param roleList 已经授予的角色集合
     * @param menuList 已经授予的菜单集合
     * @return List<String> [菜单值@权限值, ...]
     */
    @Override
    public List<String> getPermitList(UserContext userContext, List<RoleDTO> roleList, List<MenuDTO> menuList) {
        if (userContext.isAdmin()) {
            /* 管理员只需要获取所有配置即可 */
            final List<Integer> menuIds = menuList.stream().map(MenuDTO::getId).collect(Collectors.toList());
            return baseMapper.getPermitList(Wrappers.<PermitDTO>query().in("mp.menu_id", menuIds));
        }

        /* 用户则需要查询关联表 */
        final List<Integer> roleIds = roleList.stream().map(RoleDTO::getId).collect(Collectors.toList());
        final List<RoMeDTO> roMeList = roMeMapper.selectList(Wrappers.<RoMeDTO>lambdaQuery().in(RoMeDTO::getRoleId, roleIds).eq(RoMeDTO::getMenuType, "permit"));
        /* 角色菜单同时存放[菜单权限]关联ID 类型为权限时 */
        final List<Integer> mePmIds = roMeList.stream().map(RoMeDTO::getMenuId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mePmIds)) return Collections.emptyList();
        /* 最后按ID集合查找即可 */
        return baseMapper.getPermitList(Wrappers.<PermitDTO>query().in("mp.id", mePmIds));
    }

    /**
     * 按菜单ID获取下面对应的所有权限
     * @param menuId 菜单ID
     * @return List<PermitDTO>
     */
    @Override
    public List<PermitDTO> getAllocatePermitList(Integer menuId) {
        final List<MePmDTO> mepmList = mePmMapper.selectList(Wrappers.<MePmDTO>lambdaQuery().eq(MePmDTO::getMenuId, menuId));
        if (CollectionUtils.isEmpty(mepmList)) return Collections.emptyList();
        final List<Integer> permitIds = mepmList.stream().map(MePmDTO::getPermitId).collect(Collectors.toList());
        return baseMapper.selectList(Wrappers.<PermitDTO>lambdaQuery().in(PermitDTO::getId, permitIds));
    }

    /**
     * 给此菜单分配权限
     * @param dto 菜单实体
     */
    @Override
    public void allocatePermits(MenuDTO dto) {
        final Integer menuId = dto.getId();
        final List<Integer> permitIdList = dto.getPermitIds();

        final Wrapper<MePmDTO> wrapper = Wrappers.<MePmDTO>lambdaQuery().eq(MePmDTO::getMenuId, menuId);
        /* 分配的权限已经被任意角色使用时，不可对菜单重新分配 */
        final List<MePmDTO> mePms = mePmMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(mePms)) {
            final List<Integer> mePmIds = mePms.stream().map(MePmDTO::getId).collect(Collectors.toList());

            Wrapper<RoMeDTO> mePmWrapper = Wrappers.<RoMeDTO>lambdaQuery()
                    .in(RoMeDTO::getMenuId, mePmIds)
                    .eq(RoMeDTO::getMenuType, ROME_TYPE_PERMIT);
            final List<RoMeDTO> maps = roMeMapper.selectList(mePmWrapper);
            Assert.isTrue(CollectionUtils.isNotEmpty(maps), ResultMessage.SUBMIT_FAIL, "权限已被角色使用，不可重新分配");
        }

        mePmMapper.delete(wrapper);
        if (CollectionUtils.isEmpty(permitIdList)) return;
        permitIdList.forEach(permitId -> {
            final MePmDTO mePm = ObjectBuilder.builder(MePmDTO::new)
                    .with(MePmDTO::setMenuId, menuId)
                    .with(MePmDTO::setPermitId, permitId)
                    .build();
            mePm.preInsert();
            mePmMapper.insert(mePm);
        });
    }

    /**
     * 列出可授予此角色的权限集合
     * @param roleId 角色ID
     * @return List<MePmDTO>
     */
    @SuppressWarnings("all")
    @Override
    public List<MePmDTO> assignablePermits(Integer roleId) {
        /* 先找到授予这个角色的菜单集合 */
        Wrapper<RoMeDTO> condition = Wrappers.<RoMeDTO>lambdaQuery()
                .eq(RoMeDTO::getRoleId, roleId)
                .eq(RoMeDTO::getMenuType, ROME_TYPE_MENU);
        final List<RoMeDTO> roMeList = roMeMapper.selectList(condition);
        if (CollectionUtils.isEmpty(roMeList)) return Collections.emptyList();
        /* 取得所有菜单ID */
        final List<Integer> menuIds = roMeList.stream().map(RoMeDTO::getMenuId).collect(Collectors.toList());

        /* 按菜单来获取所有关联的权限集合， 菜单一[查询，新增，修改，删除], 菜单二[查询，新增，修改，删除] ... */
        Wrapper<MePmDTO> condition2 = Wrappers.<MePmDTO>lambdaQuery().in(MePmDTO::getMenuId, menuIds);
        final List<MePmDTO> mePmList = mePmMapper.getAllocatedPermitList(condition2);
        if (CollectionUtils.isEmpty(mePmList)) return Collections.emptyList();
        mePmList.forEach(MePmDTO::rebuildPermitValue);
        return mePmList;
    }

    @SuppressWarnings("all")
    @Override
    public void assignPermits(RoleDTO role) {
        final Integer roleId = role.getId();
        final List<MePmDTO> mePmList = role.getMePmList();

        /* 删除之前授予的权限配置，按角色ID关联，类型是permit */
        Wrapper<RoMeDTO> condition = Wrappers.<RoMeDTO>lambdaQuery()
                .eq(RoMeDTO::getRoleId, roleId)
                .eq(RoMeDTO::getMenuType, ROME_TYPE_PERMIT);
        roMeMapper.delete(condition);

        if (CollectionUtils.isEmpty(mePmList)) return;

        /* 按新授予的重新写入 */
        final LocalDateTime now = LocalDateTime.now();
        final String account = UserContextHolder.getUserContext().getUserAccount();
        mePmList.forEach(mePm -> {
            final RoMeDTO roMe = new RoMeDTO();
            roMe.setRoleId(roleId);
            roMe.setMenuId(mePm.getId());
            roMe.setMenuType(ROME_TYPE_PERMIT);
            roMe.setCreator(account);
            roMe.setCreateTime(now);
            roMeMapper.insert(roMe);
        });

    }

    @SuppressWarnings("all")
    @Override
    public List<MePmDTO> assignedPermits(Integer roleId) {
        /* 通过角色直接找到 类型为permit的记录即可 */
        Wrapper<RoMeDTO> condition =  Wrappers.<RoMeDTO>lambdaQuery()
                .eq(RoMeDTO::getRoleId, roleId)
                .eq(RoMeDTO::getMenuType, ROME_TYPE_PERMIT);
        final List<RoMeDTO> roMes = roMeMapper.selectList(condition);
        if (CollectionUtils.isEmpty(roMes)) return Collections.emptyList();

        final List<Integer> mePmIds = roMes.stream().map(RoMeDTO::getMenuId).collect(Collectors.toList());
        return mePmMapper.selectList(Wrappers.<MePmDTO>lambdaQuery().in(MePmDTO::getId, mePmIds));
    }
}
