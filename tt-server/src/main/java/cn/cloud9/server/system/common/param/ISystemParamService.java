package cn.cloud9.server.system.common.param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISystemParamService extends IService<SystemParamDTO> {
    IPage<SystemParamDTO> getSystemParamPage(SystemParamDTO dto);

    SystemParamDTO addSystemParam(SystemParamDTO dto);

    boolean updateSystemParam(SystemParamDTO dto);

    boolean deleteSystemParam(SystemParamDTO dto);
}
