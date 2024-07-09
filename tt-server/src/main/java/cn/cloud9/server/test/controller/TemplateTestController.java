package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.struct.util.DateUtils;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/test/ftl")
public class TemplateTestController extends BaseController {

    @GetMapping("/getTemplate")
    public void getCodeFile() throws Exception {
        Map<String, Object> paramMap = new ConcurrentHashMap<>();

        paramMap.put("modelPath", "cn.cloud9.sever.test.model");
        paramMap.put("author", "cloud9");
        paramMap.put("tableName", "info_account");
        paramMap.put("description", "测试模板代码生成");
        paramMap.put("projectName", "tt-server");
        paramMap.put("dateTime", DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT));
        paramMap.put("modelName", "InfoAccount");

        List<Map<String, String>> columns = new ArrayList<>();

        Map<String, String> field1 = new ConcurrentHashMap<>();
        field1.put("columnName", "id");
        field1.put("fieldName", "id");
        field1.put("type", "Integer");
        columns.add(field1);

        Map<String, String> field2 = new ConcurrentHashMap<>();
        field2.put("columnName", "field_2");
        field2.put("fieldName", "field2");
        field2.put("type", "String");
        columns.add(field2);

        paramMap.put("columns", columns);

        String path = this.getClass().getResource("/code-template").getFile();
        File file = new File(path);

        Configuration config = new Configuration();
        TemplateLoader loader = new FileTemplateLoader(file);
        config.setTemplateLoader(loader);

        Template template = config.getTemplate("/java/Model.java.ftl", "UTF-8");

        FileUtil.setDownloadResponseInfo(response, "TestDTO.java");
        Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        template.process(paramMap, out);
        out.flush();
        out.close();
    }
}
