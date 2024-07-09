package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.dict.annotation.Translate;
import cn.cloud9.server.struct.dict.dto.DictDTO;
import cn.cloud9.server.struct.dict.mapper.DictMapper;
import cn.cloud9.server.test.model.DictAspectModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author OnCloud9
 * @description 字典测试类
 * @project tt-server
 * @date 2022年11月12日 下午 10:15
 */
@Slf4j
@RestController(value = "testDictController")
@RequestMapping("/test/dict")
public class DictTestController {

    @Resource
    private DictMapper dictMapper;

    @Translate
    @GetMapping("/tran")
    public List<DictAspectModel> translateAspectTest() {

        /* 测试集合内的DTO能否翻译 */
        List<DictAspectModel> models = new ArrayList<>();
        for (int i = 1; i < 2; i++) {
            final DictAspectModel model = new DictAspectModel();
            model.setDictCode(1006000 + i);
            model.setMovieType("1013013 , 1013015 , 1013017 , 1013020 ");
            models.add(model);
        }

        /* 设置嵌套DTO，测试能否翻译内嵌对象 */
        final DictAspectModel model = new DictAspectModel();
        model.setDictCode(1006003);
        model.setMovieType("1013013 , 1013015 , 1013017 , 1013020 ");
        model.setStrList(Arrays.asList("Hello", "World"));
        final ArrayList<DictAspectModel> innerList = new ArrayList<>();
        innerList.add(model);
        models.get(0).setModels(innerList);

        log.info("翻译切面之前：models {}", JSON.toJSONString(models));

        return models;
    }

    /**
     * 测试我们编写的SQL执行是否有效
     * @param sql 自定义SQL
     * @return 字典集合
     */
    @GetMapping("/sql")
    public List<DictDTO> getDictListBySql(@RequestBody String sql) {
        return dictMapper.queryUsingCustomSql(sql);
    }

}
