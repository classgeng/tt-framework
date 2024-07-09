package cn.cloud9.server.struct.datauth.test;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 上午 10:46
 */
@Service("dataAuthTestService")
public class DataAuthTestServiceImpl extends ServiceImpl<DataAuthTestMapper, DataAuthTestDTO> implements IDataAuthTestService {

}
