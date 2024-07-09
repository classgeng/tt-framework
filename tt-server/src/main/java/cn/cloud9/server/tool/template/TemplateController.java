package cn.cloud9.server.tool.template;

import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.response.NoApiResult;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/template")
public class TemplateController extends BaseController {

    @Resource
    private TemplateService templateService;

    /**
     * 列出模板类型集合
     * @return  List<Map<String, Object>> 模板类型集合
     */
    @GetMapping("/list")
    public List<Map<String, Object>> listTemplateType() {
        return TemplateType.getAvailableTypes()
            .stream()
            .map(BeanUtil::beanToMap)
            .collect(Collectors.toList());
    }

    /**
     * 下载模板生成文件
     * @param tp TemplateParam 模板参数
     * @throws Exception 执行异常
     */
    @PostMapping(value = "/download", produces = MediaType.TEXT_PLAIN_VALUE)
    public void templateDownload(@RequestBody TemplateParam tp) throws Exception {
        templateService.downloadTemplateFile(response, tp);
    }

    /**
     * 下载全部模板文件
     * @param tp
     */
    @NoApiResult
    @PostMapping(value = "/download-all")
    public void templateDownloadAll(@RequestBody TemplateParam tp) throws Exception {
        templateService.downloadAllTemplateFile(response, tp);
    }

    /**
     * 获取模板生成的输出内容
     * @param tp 模板参数
     * @return 输出内容
     * @throws Exception IO异常
     */
    @PostMapping("/content")
    public String getTemplateContent(@RequestBody TemplateParam tp) throws Exception {
        return templateService.getTemplateContent(tp, tp.getTemplateTypeByCode());
    }


}
