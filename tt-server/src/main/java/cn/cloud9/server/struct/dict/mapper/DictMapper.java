package cn.cloud9.server.struct.dict.mapper;

import cn.cloud9.server.struct.dict.dto.DictDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DictMapper extends BaseMapper<DictDTO> {

    @Select("${SQL}")
    List<DictDTO> queryUsingCustomSql(@Param("SQL") String sql);
}
