package cn.cloud9.server.tool.template;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.tool.ds.DbConnServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("templateService")
public class TemplateService {

    @Resource
    private FileProperty fileProperty;

    /* 模板文件根目录 */
    private static final String ROOT_PATH = "/code-template";
    private static Configuration configuration;

    static {
        try {
            String path = TemplateService.class.getResource(ROOT_PATH).getFile();
            File file = new File(path);
            TemplateLoader loader = new FileTemplateLoader(file);
            configuration = new Configuration(Configuration.getVersion());
            configuration.setTemplateLoader(loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载模板生成文件
     * @param response 响应对象
     * @param tp 模板参数
     * @throws Exception IO异常
     */
    public void downloadTemplateFile(HttpServletResponse response, TemplateParam tp) throws Exception {
        TemplateType tt = tp.getTemplateTypeByCode();
        tp.setDateTime();
        if (TemplateType.MODEL.equals(tt)) getColumnsMetaData(tp);
        Assert.isTrue(TemplateType.NONE.equals(tt), ResultMessage.NOT_FOUND_ERROR, "模板类型编号");
        Map<String, Object> paramMap = BeanUtil.beanToMap(tp);

        Template template = configuration.getTemplate(tt.getPath(), "UTF-8");
        FileUtil.setDownloadResponseInfo(response, paramMap.get(tt.getFilenameKey()) + tt.getSuffix());
        Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        /* 输出成型的文件 */
        template.process(paramMap, out);
        out.flush();
        out.close();
    }

    /**
     * 获取模板生成的输出内容
     * @param tp 模板参数
     * @return 输出内容
     * @throws Exception IO异常
     */
    public String getTemplateContent(TemplateParam tp, TemplateType tt) throws Exception {
        tp.setDateTime();
        if (TemplateType.MODEL.equals(tt)) getColumnsMetaData(tp);
        Assert.isTrue(TemplateType.NONE.equals(tt), ResultMessage.NOT_FOUND_ERROR, "模板类型编号");
        Map<String, Object> paramMap = BeanUtil.beanToMap(tp);
        Template template = configuration.getTemplate(tt.getPath(), "UTF-8");

        /* 代替方法 String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap); */
        StringWriter result = new StringWriter(1024);
        template.process(paramMap, result);
        return result.toString();
    }


    /**
     * 下载全部模板文件
     * @param response
     * @param tp
     */
    public void downloadAllTemplateFile(HttpServletResponse response, TemplateParam tp) throws Exception {
        final String cachePath = fileProperty.getBaseDirectory() + ROOT_PATH;
        File file = new File(cachePath);
        if (!file.exists()) file.mkdirs();

        List<TemplateType> templateTypes = TemplateType.getAvailableTypes();
        List<File> cacheFiles = new ArrayList<>(templateTypes.size());
        for (TemplateType templateType : templateTypes) {
            /* 获取输出的文件名 */
            String fileName = ReflectUtil.getFieldValue(tp, templateType.getFilenameKey()).toString();
            fileName = fileName + templateType.getSuffix();

            /* 创建模板文件 */
            String fullPath = cachePath + File.separator + fileName;
            File templateFile = new File(fullPath);
            if (!templateFile.exists()) file.createNewFile();

            /* 获取输出的成型内容 */
            tp.setTemplateCode(templateType.getCode());
            String content = this.getTemplateContent(tp, templateType);

            /* 写入本地文件 */
            FileOutputStream fos = new FileOutputStream(fullPath);
            fos.write(content.getBytes());
            fos.close();
            cacheFiles.add(templateFile);
        }

        /* 创建压缩文件 */
        String zipFileName = cachePath + File.separator + tp.getTableName() + ".zip";
        File zipFile = new File(zipFileName);
        ZipUtil.zip(zipFile, false, cacheFiles.toArray(new File[]{}));

        /* 给压缩文件设置信息 */
        FileUtil.setDownloadResponseInfo(response, zipFile, zipFile.getName());

        /* 输出压缩文件 */
        BufferedInputStream in = cn.hutool.core.io.FileUtil.getInputStream(zipFile.getAbsolutePath());
        ServletOutputStream os = response.getOutputStream();
        IoUtil.copy(in, os, IoUtil.DEFAULT_BUFFER_SIZE);
        in.close();
        os.close();

        /* 删除本地缓存 */
        zipFile.delete();
        cacheFiles.forEach(File::delete);
    }

    /**
     * 获取列的元数据
     * @param tp
     */
    private void getColumnsMetaData(TemplateParam tp) throws Exception {
        Db db = Db.use(DbConnServiceImpl.getDsById(tp.getDbConnId()));
        String querySql =
            "SELECT\n" +
            "\tCOLUMN_NAME AS `columnName`,\n" +
            "\tREPLACE(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(\n" +
            "\tLOWER(COLUMN_NAME), '_a','A'),'_b','B'),'_c','C'),'_d','D'),'_e','E'),'_f','F'),'_g','G'),'_h','H'),'_i','I'),'_j','J'),'_k','K'),'_l','L'),'_m','M'),'_n','N'),'_o','O'),'_p','P'),'_q','Q'),'_r','R'),'_s','S'),'_t','T'),'_u','U'),'_v','V'),'_w','W'),'_x','X'),'_y','Y'),'_z','Z') AS `fieldName`,\n" +
            "\tCASE\n" +
            "\t\tWHEN DATA_TYPE ='bit' THEN 'Boolean'\n" +
            "\t\tWHEN DATA_TYPE ='tinyint' THEN 'Byte'\n" +
            "\t\tWHEN LOCATE(DATA_TYPE, 'smallint,mediumint,int,integer') > 0 THEN 'Integer'\n" +
            "\t\tWHEN DATA_TYPE = 'bigint' THEN 'Long'\n" +
            "\t\tWHEN LOCATE(DATA_TYPE, 'float, double') > 0 THEN 'Double'\n" +
            "\t\tWHEN DATA_TYPE = 'decimal' THEN 'BigDecimal'\n" +
            "\t\tWHEN LOCATE(DATA_TYPE, 'time, date, year, datetime, timestamp') > 0 THEN 'LocalDateTime'\n" +
            "\t\tWHEN LOCATE(DATA_TYPE, 'json, char, varchar, tinytext, text, mediumtext, longtext') > 0 THEN 'String'\n" +
            "\tELSE 'String' END AS `type`\n" +
            "FROM\n" +
            "\t`information_schema`.`COLUMNS`\n" +
            "WHERE\n" +
            "\t`TABLE_SCHEMA` = ? \n" +
            "\tAND `TABLE_NAME` = ? ";
        List<Entity> entityList = db.query(querySql, tp.getSchemaName(), tp.getTableName());
        tp.setColumns(entityList);
    }

}
