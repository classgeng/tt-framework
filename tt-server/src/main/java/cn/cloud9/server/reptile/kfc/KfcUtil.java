package cn.cloud9.server.reptile.kfc;

import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import java.net.HttpCookie;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class KfcUtil {

    private static final Db db = Db.use(SpringContextHolder.getBean(DataSource.class));

    public static void start() {
        /* 初始化表 */
        initialTableSpace();

        /* 获取基础信息 */
        final BasicInfo basicInfo = getBasicInfo();
        final List<String> cityNames = basicInfo.getCityNames();
        final List<HttpCookie> cookies = basicInfo.getCookies();

        /* 读取门店，并写入 */
        cityNames.forEach(cityName -> getPagingData(cityName, 1, 50, cookies));

        /* 去重，重命名表 */
        clearDistinctData();
        changeTableName();
    }
    
    /**
     * 获取Cookie和基础请求参数
     * // liList.forEach(li -> {
     * //     final String provinceName = li.getElementsByTag("strong").get(0).text();
     * //     final Elements aList = li.select("div.shen_city > a");
     * //     aList.forEach(aTag -> {
     * //         String cityName = aTag.text();
     * //         cityNames.add(cityName);
     * //     });
     * // });
     * @return
     */
    public static BasicInfo getBasicInfo() {
        final HttpResponse httpResponse = HttpUtil.createGet(KfcConstant.KFC_STORE_CLIENT_PAGE).addHeaders(new HashMap<String, String>() {{
            this.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            this.put("Upgrade-Insecure-Requests", "1");
            this.put("Referer", "http://www.kfc.com.cn/kfccda/news.aspx");
            this.put("Host", "www.kfc.com.cn");
            this.put("Connection", "keep-alive");
            this.put("Cache-Control", "max-age=0");
            this.put("Accept-Language", "zh-CN,zh;q=0.9");
            this.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        }}).execute();
        final List<HttpCookie> cookies = httpResponse.getCookies();
        final String body = httpResponse.body();
        final Document document = Jsoup.parse(body);
        final Elements liList = document.select("ul.shen_info > li");
        List<String> cityNames = new ArrayList<>();
        liList.forEach(li -> li.select("div.shen_city > a").forEach(aTag -> cityNames.add(aTag.text())));
        return BasicInfo.builder().cityNames(cityNames).cookies(cookies).build();
    }


    /**
     * 请求门店翻页查询接口时携带的基本信息
     * @return
     */
    public static Map<String, String> getNewBasicHeader() {
        return new HashMap<String, String>() {{
            this.put("Accept", "application/json, text/javascript, */*; q=0.01");
            this.put("Accept-Language", "zh-CN,zh;q=0.9");
            this.put("Connection", "keep-alive");
            this.put("Content-Length", "55");
            this.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            this.put("Host", "www.kfc.com.cn");
            this.put("Origin", "http://www.kfc.com.cn");
            this.put("Referer", "http://www.kfc.com.cn/kfccda/storelist/index.aspx");
            this.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            this.put("X-Requested-With", "XMLHttpRequest");
        }};
    }

    /**
     * 按城市名和翻页参数获取接口数据
     * @param cityName
     * @param pageIndex
     * @param pageSize
     * @param cookies
     * @return
     */
    public static void getPagingData(String cityName, Integer pageIndex, Integer pageSize, List<HttpCookie> cookies) {
        final Integer PAGE_SIZE = null == pageSize ? KfcConstant.PAGE_SIZE : pageSize;
        final Map<String, String> header = getNewBasicHeader();
        header.put("Cookie", cookies.stream().map(HttpCookie::toString).collect(Collectors.joining("; ")));
        final HttpResponse response = HttpUtil.createPost(KfcConstant.KFC_STORE_PAGING_QUERY_API).addHeaders(header).formStr(new HashMap<String, String>() {{
            this.put("cname", cityName);
            this.put("pid", "");
            this.put("pageIndex", pageIndex.toString());
            this.put("pageSize", PAGE_SIZE.toString());
        }}).execute();

        final String body = response.body();
        final Map<String, Object> map = JSON.parseObject(body, Map.class);
        final Integer totalCount = getTotalCount(map.get("Table").toString());
        final List<KfcStoreDTO> storeList = JSONObject.parseArray(map.get("Table1").toString(), KfcStoreDTO.class);

        boolean isEmpty = CollUtil.isEmpty(storeList);
        if (isEmpty) return;
        storeList.forEach(store -> writeStoreInfoToDB(store));
        getPagingData(cityName, pageIndex + 1, PAGE_SIZE, cookies);
    }

    /**
     * 初始化建表
     */
    @SneakyThrows
    public static void initialTableSpace() {
        final String SQL =
                "CREATE TABLE IF NOT EXISTS `kfc_store`  (\n" +
                        "  `id` int NOT NULL AUTO_INCREMENT COMMENT 'KFC门店ID',\n" +
                        "  `store_name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '门店名称',\n" +
                        "  `province` varchar(12) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所在省份',\n" +
                        "  `city` varchar(12) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所在城市',\n" +
                        "  `address` varchar(254) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '具体地址',\n" +
                        "  `tag` varchar(48) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '相关特性',\n" +
                        "  `gen_time` datetime DEFAULT NULL COMMENT '记录创建时间',\n" +
                        "  PRIMARY KEY (`ID`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='KFC门店统计表';";
        db.execute(SQL);
    }


    /**
     * 删除重复记录
     */
    @SneakyThrows
    public static void clearDistinctData() {
        final String SQL = "DELETE FROM `kfc_store`\n" +
                "WHERE ID NOT IN (SELECT * FROM (SELECT MAX(ID) FROM `kfc_store` GROUP BY store_name) AS DIFF)\n;";
        db.execute(SQL);
    }

    /**
     * 更名备注
     */
    @SneakyThrows
    public static void changeTableName() {
        Date date = new Date();
        long dateTime = date.getTime();
        final String SQL = "RENAME TABLE `kfc_store` TO `kfc_store_" + dateTime + "`;";
        db.execute(SQL);
    }


    /**
     * 写db操作
     * @param storePO
     */
    @SneakyThrows
    private static void writeStoreInfoToDB(KfcStoreDTO storePO) {
        db.insert(Entity.create("`kfc_store`")
                .set("STORE_NAME", storePO.getStoreName())
                .set("PROVINCE", storePO.getProvinceName())
                .set("CITY", storePO.getCityName())
                .set("ADDRESS", storePO.getProvinceName() + "|" + storePO.getCityName() + "|" + storePO.getAddressDetail())
                .set("TAG", storePO.getPro())
                .set("GEN_TIME", LocalDateTime.now())
        );
    }

    /**
     * 获取接口提供的总记录数
     * @param jsonData
     * @return
     */
    private static Integer getTotalCount(String jsonData) {
        final Matcher matcher = KfcConstant.TOTAL_PATTERN.matcher(jsonData);
        if (!matcher.find()) return 0;
        return Integer.valueOf(matcher.group().replace("\"rowcount\":", "").trim());
    }

}
