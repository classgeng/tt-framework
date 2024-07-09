package cn.cloud9.server.system.common.param;

import cn.cloud9.server.struct.constant.Constant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 下午 11:22
 */
@Service("systemParamService")
public class SystemParamServiceImpl extends ServiceImpl<SystemParamMapper, SystemParamDTO> implements ISystemParamService {
    @Override
    public IPage<SystemParamDTO> getSystemParamPage(SystemParamDTO dto) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getParamName()), SystemParamDTO::getParamName, dto.getParamName())
                .like(StringUtils.isNotBlank(dto.getParamValue()), SystemParamDTO::getParamValue, dto.getParamValue())
                .orderByDesc(SystemParamDTO::getCreateTime)
                .page(dto.getPage());
    }

    @Override
    public SystemParamDTO addSystemParam(SystemParamDTO dto) {
        dto.preInsert();
        final int insert = baseMapper.insert(dto);
        return Constant.RES_DONE.equals(insert) ? dto : null;
    }

    @Override
    public boolean updateSystemParam(SystemParamDTO dto) {
        dto.preUpdate();
        final int update = baseMapper.updateById(dto);
        return Constant.RES_DONE.equals(update);
    }

    @Override
    public boolean deleteSystemParam(SystemParamDTO dto) {
        final int delete = baseMapper.deleteById(dto.getId());
        return Constant.RES_DONE.equals(delete);
    }
}
