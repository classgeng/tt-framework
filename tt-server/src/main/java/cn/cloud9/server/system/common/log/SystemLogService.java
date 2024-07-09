package cn.cloud9.server.system.common.log;

import cn.cloud9.server.struct.log.SystemLogDAO;
import cn.cloud9.server.struct.log.SystemLogDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月11日 下午 09:56
 */
@Service("systemLogService")
public class SystemLogService extends ServiceImpl<SystemLogDAO, SystemLogDTO> {

    public void flushSystemLog() {
        baseMapper.flushSystemLog();
    }
}
