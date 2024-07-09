package cn.cloud9.server.struct.cxf.abbr.service;

import cn.cloud9.server.struct.cxf.abbr.AbbrWordDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface JaxWsAbbrWordService {
    boolean saveWord(AbbrWordDTO word);
    boolean deleteWord(Integer code);
    boolean updateWord(AbbrWordDTO word);
    List<AbbrWordDTO> listWords();
    Page<AbbrWordDTO> pageWords(AbbrWordDTO word);
    AbbrWordDTO findWordById(Integer code);
}
