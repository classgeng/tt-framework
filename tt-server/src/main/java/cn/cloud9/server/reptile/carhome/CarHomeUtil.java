package cn.cloud9.server.reptile.carhome;

import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarHomeUtil {

    private static final Db db = Db.use(SpringContextHolder.getBean(DataSource.class));
    private static final String HOST = "https://www.autohome.com.cn/grade/carhtml/${char}.html";

    @SneakyThrows
    public static void initializeTableSpace() {
        String sql =
            "CREATE TABLE `car_series` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `brand` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌',\n" +
            "  `brand_logo` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌LOGO',\n" +
            "  `provider` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商',\n" +
            "  `provider_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商链接',\n" +
            "  `series` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车系',\n" +
            "  `series_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车系链接',\n" +
            "  `suggest_price` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '建议价格',\n" +
            "  `suggest_price_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '建议价格地址',\n" +
            "  `other_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '其他详情',\n" +
            "  `gen_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ")\n" +
            "ENGINE=InnoDB\n" +
            "AUTO_INCREMENT=1\n" +
            "DEFAULT CHARSET=utf8mb4\n" +
            "COLLATE=utf8mb4_general_ci\n" +
            "COMMENT='汽车之家-车系信息 https://www.autohome.com.cn/grade/carhtml/${A-Z}.html';";
        db.execute(sql);
    }

    @SneakyThrows
    public static void renameSeriesTable() {
        db.execute("RENAME TABLE `car_series` to `car_series_" + new Date().getTime() + "`;");
    }

    public static void start() {
        initializeTableSpace();
        List<String> seriesLinkList = getAllSeriesLink();
        seriesLinkList.forEach(url -> {
            final String htmlCode = HttpUtil.get(url);
            final Document seriesPage = Jsoup.parse(htmlCode);

            final Elements dlList = seriesPage.getElementsByTag("dl");
            if (CollUtil.isEmpty(dlList)) return;

            dlList.forEach(dl -> {

                // 品牌logo
                final Element img = dl.getElementsByTag("img").get(0);
                final String imgUrl = img.attr("src").replace("//", "https://");
                // final InputStream inputStream = HttpUtil.createGet(imgUrl).execute().bodyStream();

                // 品牌
                final String carBrand = dl.getElementsByTag("div").get(0).text();

                // 国内供应商
                final Elements h3Titles = dl.getElementsByClass("h3-tit");
                final Elements seriesUlList = dl.getElementsByClass("rank-list-ul");

                h3Titles.forEach(h3title -> {
                    // 获取供应商和对应的地址
                    final String providerName = h3title.text();
                    final String providerHref = h3title.getElementsByTag("a").attr("href");

                    // 得到当前下标
                    final int index = h3Titles.indexOf(h3title);
                    final Element seriesUl = seriesUlList.get(index);
                    final Elements liList = seriesUl.getElementsByTag("li");
                    liList.forEach(li -> {
                        final Elements h4s = li.getElementsByTag("h4");
                        if (CollUtil.isEmpty(h4s)) return;
                        final Element seriesEl = h4s.get(0).getElementsByTag("a").get(0);
                        final String seriesName = seriesEl.text();
                        final String seriesHref = seriesEl.attr("href");

                        final Elements reds = li.getElementsByClass("red");
                        boolean hasPriceSuggest = !CollUtil.isEmpty(reds);
                        final String suggestPrice = hasPriceSuggest  ? reds.get(0).text() : null;
                        final String suggestPriceHref = hasPriceSuggest ? reds.get(0).attr("href") : null;

                        final Elements divs = li.getElementsByTag("div");
                        final Element lastDiv = divs.get(divs.size() - 1);
                        final String otherInfo = lastDiv.html();

                        try {
                            db.insert(Entity.create("car_series")
                                    .set("BRAND", carBrand)
                                    .set("BRAND_LOGO", imgUrl)
                                    .set("PROVIDER", providerName)
                                    .set("PROVIDER_URL", providerHref)
                                    .set("SERIES", seriesName)
                                    .set("SERIES_URL", seriesHref)
                                    .set("SUGGEST_PRICE", suggestPrice)
                                    .set("SUGGEST_PRICE_URL", suggestPriceHref)
                                    .set("OTHER_INFO", otherInfo)
                                    .set("GEN_TIME", LocalDateTime.now())
                            );
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        });
        renameSeriesTable();
    }

    @SneakyThrows
    public static List<String> getAllSeriesLink() {
        List<String> seriesLinkList = new ArrayList<>();
        final char START = 'a', END = 'z';
        for (int i = START; i <= END; i++) {
            String currentHost = HOST.replace("${char}", String.valueOf((char) i));
            seriesLinkList.add(currentHost);
        }
        return seriesLinkList;
    }





}
