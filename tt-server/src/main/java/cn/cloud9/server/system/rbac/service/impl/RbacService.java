package cn.cloud9.server.system.rbac.service.impl;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.dto.map.RoUsDTO;
import cn.cloud9.server.system.rbac.mapper.map.MePmMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoMeMapper;
import cn.cloud9.server.system.rbac.mapper.map.RoUsMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月17日 下午 07:58
 */
@Service("rbacService")
public class RbacService {

    @Resource
    private MePmMapper mePmMapper;

    @Resource
    private RoMeMapper roMeMapper;

    @Resource
    private RoUsMapper roUsMapper;


    /**
     * 删除菜单和权限的安全校验
     * @param dto 菜单权限关联实体
     */
    public void deleteMenuOrPermitSafetyCheck(MePmDTO dto) {
        final Integer menuId = dto.getMenuId();
        final Integer permitId = dto.getPermitId();

        Wrapper<MePmDTO> query = Wrappers.<MePmDTO>lambdaQuery()
                .eq(Objects.nonNull(permitId), MePmDTO::getPermitId, permitId)
                .eq(Objects.nonNull(menuId), MePmDTO::getMenuId, menuId);
        final List<MePmDTO> mePms = mePmMapper.selectList(query);
        final boolean isExists = CollectionUtils.isNotEmpty(mePms);

        Assert.isTrue(isExists, ResultMessage.SUBMIT_FAIL, "[菜单]或[权限]存在关联配置");
    }

    /**
     * 删除菜单和角色的安全校验
     * @param dto 菜单角色关联实体
     */
    public void deleteRoleOrMenuSafetyCheck(RoMeDTO dto) {
        final Integer menuId = dto.getMenuId();
        final Integer roleId = dto.getRoleId();

        Wrapper<RoMeDTO> query = Wrappers.<RoMeDTO>lambdaQuery()
                .eq(Objects.nonNull(roleId), RoMeDTO::getRoleId, roleId)
                .eq(Objects.nonNull(menuId), RoMeDTO::getMenuId, menuId);
        final List<RoMeDTO> mePms = roMeMapper.selectList(query);
        final boolean isExists = CollectionUtils.isNotEmpty(mePms);

        Assert.isTrue(isExists, ResultMessage.SUBMIT_FAIL, "[菜单]或[角色]存在关联配置");
    }

    /**
     * 删除用户和角色的安全校验
     * @param dto 用户角色关联实体
     */
    public void deleteRoleOrUserSafetyCheck(RoUsDTO dto) {
        final Integer userId = dto.getUserId();
        final Integer roleId = dto.getRoleId();

        Wrapper<RoUsDTO> query = Wrappers.<RoUsDTO>lambdaQuery()
                .eq(Objects.nonNull(roleId), RoUsDTO::getRoleId, roleId)
                .eq(Objects.nonNull(userId), RoUsDTO::getUserId, userId);
        final List<RoUsDTO> mePms = roUsMapper.selectList(query);
        final boolean isExists = CollectionUtils.isNotEmpty(mePms);

        Assert.isTrue(isExists, ResultMessage.SUBMIT_FAIL, "[用户]或[角色]存在关联配置");
    }


}
