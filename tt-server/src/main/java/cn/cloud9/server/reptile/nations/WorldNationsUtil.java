package cn.cloud9.server.reptile.nations;

import cn.cloud9.server.struct.spring.SpringContextHolder;
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
import java.util.Arrays;
import java.util.Date;

public class WorldNationsUtil {
    private static final Db db = Db.use(SpringContextHolder.getBean(DataSource.class));
    private static final String[] LINKS = {
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/yz_676205/",
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/fz_677316/",
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/oz_678770/",
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/bmz_679954/",
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/nmz_680924/",
            "https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/dyz_681240/",
    };
    /**
     * 官网的跳转逻辑
     * https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/yz_676205/1206_676207/1206x0_676209/
     *
     * @param baseUrl https://www.mfa.gov.cn/web/gjhdq_676201/gj_676203/yz_676205/
     * @param source ./1206_676207/
     * @return
     */
    private static String hrefJoinRule(String baseUrl, String source) {
        String replace = source.replace("./", "");
        final String detail = HttpUtil.get(baseUrl + replace);

        final Document redirectDoc = Jsoup.parse(detail);
        final Elements scriptList = redirectDoc.getElementsByTag("script");
        final String data = scriptList
                .stream()
                .filter(script -> script.data().contains("window.location.href"))
                .findFirst()
                .get().data();
        String substring = data.substring(data.indexOf("./"), data.length() - 3);
        substring = substring.replace("./", "");
        return baseUrl + replace + substring;
    }

    @SneakyThrows
    public static void start() {
        String sql =
            "CREATE TABLE `world_nations` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家名称',\n" +
            "  `full_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家全称',\n" +
            "  `link` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接地址',\n" +
            "  `continent` varchar(22) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '所属洲',\n" +
            "  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '详细',\n" +
            "  `gen_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='世界国家';";
        db.execute(sql);

        Arrays.asList(LINKS).forEach(link -> {
            final String htmlCode = HttpUtil.get(link);
            final Document document = Jsoup.parse(htmlCode);
            final Element linkList2 = document.getElementsByClass("linkList2").get(0);
            final String CONTINENT = linkList2.getElementsByClass("linkHd").get(0).text();

            linkList2.getElementsByTag("a").forEach(aTag -> {
                final String NATION_NAME = aTag.text();

                final String targetHref = hrefJoinRule(link, aTag.attr("href"));
                final String detailHtml = HttpUtil.get(targetHref);
                final Document detailDoc = Jsoup.parse(detailHtml);
                final Element content = detailDoc.getElementById("content");
                final Elements pList = content.getElementsByTag("p");
                String FULL_NAME = pList.get(0).text();
                final String DETAIL = content.text();
                try {
                    db.insert(Entity
                            .create("world_nations")
                            .set("name", NATION_NAME)
                            .set("link", targetHref)
                            .set("full_name", FULL_NAME)
                            .set("continent", CONTINENT)
                            .set("detail", DETAIL)
                            .set("gen_time", LocalDateTime.now()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });

        db.execute("RENAME TABLE `world_nations` TO `world_nations_" + new Date().getTime() + "`");
    }
}
