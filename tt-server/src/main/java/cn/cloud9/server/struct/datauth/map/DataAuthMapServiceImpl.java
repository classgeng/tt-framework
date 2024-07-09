package cn.cloud9.server.struct.datauth.map;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import cn.cloud9.server.struct.datauth.config.DataAuthConfigMapper;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 下午 07:45
 */
@Service("dataAuthMapService")
public class DataAuthMapServiceImpl extends ServiceImpl<DataAuthMapMapper, DataAuthMapDTO> implements IDataAuthMapService {

    @Resource
    private DataAuthConfigMapper dataAuthConfigMapper;

    @Override
    public List<DataAuthConfigDTO> getAssignedList(Integer userId) {
        final List<DataAuthMapDTO> mapList = lambdaQuery().eq(DataAuthMapDTO::getUserId, userId).list();
        if (CollectionUtils.isEmpty(mapList)) return Collections.emptyList();

        List<DataAuthConfigDTO> configs = new ArrayList<>(mapList.size());
        for (DataAuthMapDTO daMap : mapList) {
            final DataAuthConfigDTO config = dataAuthConfigMapper.selectById(daMap.getConfigId());
            config.setAuthValue(daMap.getAuthValue());
            configs.add(config);
        }

        return configs;
    }

    @Override
    public void assign(UserDTO dto) {
        final Integer userId = dto.getId();
        baseMapper.delete(Wrappers.<DataAuthMapDTO>lambdaQuery().eq(DataAuthMapDTO::getUserId, userId));

        final List<DataAuthConfigDTO> dataAuthConfigs = dto.getDataAuthConfigs();
        if (CollectionUtils.isEmpty(dataAuthConfigs)) return;

        LocalDateTime now = LocalDateTime.now();
        final UserContext userContext = UserContextHolder.getUserContext();
        for (DataAuthConfigDTO dataAuthConfig : dataAuthConfigs) {
            DataAuthMapDTO daMap = new DataAuthMapDTO();
            daMap.setUserId(userId);
            daMap.setConfigId(dataAuthConfig.getId());
            daMap.setIdentKey(dataAuthConfig.getIdentKey());
            daMap.setAuthValue(StringEscapeUtils.unescapeHtml4(dataAuthConfig.getAuthValue()));
            daMap.setCreator(userContext.getUserAccount());
            daMap.setCreateTime(now);
            baseMapper.insert(daMap);
        }
    }
}
