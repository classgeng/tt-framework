package cn.cloud9.server.struct.cxf.abbr.service;

import cn.cloud9.server.struct.cxf.abbr.AbbrWordDTO;
import cn.cloud9.server.struct.cxf.abbr.AbbrWordMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月28日 下午 09:40
 */
@Service("abbrWordService")
public class AbbrWordServiceImpl extends ServiceImpl<AbbrWordMapper, AbbrWordDTO> implements JaxRsAbbrWordService, JaxWsAbbrWordService {

    @Override
    public boolean saveWord(AbbrWordDTO word) {
        final int insert = baseMapper.insert(word);
        return 1 == insert;
    }

    @Override
    public boolean deleteWord(Integer code) {
        final int delete = baseMapper.deleteById(code);
        return 1 == delete;
    }

    @Override
    public boolean updateWord(AbbrWordDTO word) {
        final int update = baseMapper.updateById(word);
        return 1 == update;
    }

    @Override
    public List<AbbrWordDTO> listWords() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public Page<AbbrWordDTO> pageWords(AbbrWordDTO word) {
        return baseMapper.selectPage(word.getPage(), Wrappers.emptyWrapper());
    }

    @Override
    public AbbrWordDTO findWordById(Integer code) {
        return baseMapper.selectById(code);
    }
}
