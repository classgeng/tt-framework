package cn.cloud9.server.struct.log;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

public interface SystemLogDAO extends BaseMapper<SystemLogDTO> {

    @Delete("TRUNCATE TABLE `sys_log`")
    void flushSystemLog();

}
