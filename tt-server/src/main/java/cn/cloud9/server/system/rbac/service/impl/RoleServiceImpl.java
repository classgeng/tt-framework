package cn.cloud9.server.system.rbac.service.impl;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.lambda.ObjectBuilder;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import cn.cloud9.server.system.rbac.dto.map.RoUsDTO;
import cn.cloud9.server.system.rbac.mapper.RoleMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoUsMapper;
import cn.cloud9.server.system.rbac.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginContext;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 上午 12:14
 */
@Slf4j
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDTO> implements IRoleService {

    @Resource
    private RoUsMapper roUsMapper;

    @Override
    public IPage<RoleDTO> queryRolePage(RoleDTO dto) {
        return lambdaQuery()
            .like(StringUtils.isNotBlank(dto.getRoleName()), RoleDTO::getRoleName, dto.getRoleName())
            .like(StringUtils.isNotBlank(dto.getRoleValue()), RoleDTO::getRoleValue, dto.getRoleValue())
            .between(StringUtils.isNotBlank(dto.getStartCreateTime()) && StringUtils.isNotBlank(dto.getEndCreateTime()),
                RoleDTO::getCreateTime,
                dto.getStartCreateTime(),
                dto.getEndCreateTime())
            .orderByDesc(RoleDTO::getCreateTime)
            .page(dto.getPage());
    }

    @Override
    public List<RoleDTO> getRoleList(UserContext userContext) {
        /* 1、查询关联的角色ID集合 */
        final Long userId = userContext.getUserId();
        final List<RoUsDTO> roUsList = roUsMapper.selectList(Wrappers.<RoUsDTO>lambdaQuery().eq(RoUsDTO::getUserId, userId));

        /* 2、角色ID集合再查询角色表信息返回 */
        final List<Integer> roleIds = roUsList.stream().map(RoUsDTO::getRoleId).collect(Collectors.toList());
        return lambdaQuery().in(RoleDTO::getId, roleIds).list();
    }

    @Override
    public List<RoleDTO> getAssignedRoleList(Integer sysUserId) {
        final List<RoUsDTO> roUsList = roUsMapper.selectList(Wrappers.<RoUsDTO>lambdaQuery().eq(RoUsDTO::getUserId, sysUserId));
        final boolean empty = CollectionUtils.isEmpty(roUsList);
        if (empty) return Collections.emptyList();
        final List<Integer> roleIds = roUsList.stream().map(RoUsDTO::getRoleId).collect(Collectors.toList());
        return lambdaQuery().in(RoleDTO::getId, roleIds).list();
    }

    @Override
    public void assignedRoles(UserDTO user) {
        final Integer userId = user.getId();
        final List<Integer> roleIds = user.getRoleIds();
        roUsMapper.delete(Wrappers.<RoUsDTO>lambdaQuery().eq(RoUsDTO::getUserId, userId));
        if (CollectionUtils.isEmpty(roleIds)) return;
        roleIds.forEach(roleId -> {
            final RoUsDTO roUs = ObjectBuilder.builder(RoUsDTO::new)
                    .with(RoUsDTO::setUserId, userId)
                    .with(RoUsDTO::setRoleId, roleId)
                    .with(RoUsDTO::setCreator, UserContextHolder.getUserContext().getUserAccount())
                    .with(RoUsDTO::setCreateTime, LocalDateTime.now())
                    .build();
            roUsMapper.insert(roUs);
        });
    }
}
