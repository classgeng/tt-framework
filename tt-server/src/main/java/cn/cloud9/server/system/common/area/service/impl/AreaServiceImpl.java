package cn.cloud9.server.system.common.area.service.impl;

import cn.cloud9.server.system.common.area.dto.AreaDTO;
import cn.cloud9.server.system.common.area.mapper.AreaMapper;
import cn.cloud9.server.system.common.area.service.IAreaService;
import cn.cloud9.server.struct.util.tree.TreeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月19日 下午 08:06
 */
@Slf4j
@Service("areaService")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, AreaDTO> implements IAreaService, IService<AreaDTO> {

    @Override
    public List<AreaDTO> getAreaList(AreaDTO area) {
        final List<AreaDTO> list = lambdaQuery()
                .eq(Objects.nonNull(area.getAreaLevel()), AreaDTO::getAreaLevel, area.getAreaLevel())
                .eq(Objects.nonNull(area.getParentId()), AreaDTO::getParentId, area.getParentId())
                .list();
        return list;
    }

    @Override
    public List<AreaDTO> getAreaTree() {
        final List<AreaDTO> areas = lambdaQuery().list();
        return TreeUtil.build(areas);
    }

    @Override
    public IPage<AreaDTO> getAreaPage(AreaDTO dto) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getAreaName()), AreaDTO::getAreaName, dto.getAreaName())
                .in(Objects.nonNull(dto.getId()), AreaDTO::getId, getAllAreaIdStartWith(dto))
                .eq(Objects.nonNull(dto.getAreaLevel()), AreaDTO::getAreaLevel, dto.getAreaLevel())
                .page(dto.getPage());
    }

    /**
     * 获取全部子节点，包括节点本身
     * @param dto 区域实体
     * @return 全部节点ID
     */
    private List<Long> getAllAreaIdStartWith(AreaDTO dto) {
        /* 无主键不执行 */
        final Long id = dto.getId();
        final boolean noId = Objects.isNull(id);
        if (noId) return Collections.emptyList();

        List<Long> allIds = new ArrayList<>();
        allIds.add(id);
        this.getAreaIdsRecursive(allIds, id);

        return allIds;
    }

    /**
     * 递归获取后代节点
     * @param areaIds 全部节点ID容器
     * @param parentId 父节点
     */
    private void getAreaIdsRecursive(List<Long> areaIds, Long parentId) {
        final List<AreaDTO> list = lambdaQuery().eq(AreaDTO::getParentId, parentId).list();
        if (CollectionUtils.isEmpty(list)) return;

        for (AreaDTO area : list) {
            final Long areaId = area.getId();
            areaIds.add(areaId);
            getAreaIdsRecursive(areaIds, areaId);
        }
    }
}
