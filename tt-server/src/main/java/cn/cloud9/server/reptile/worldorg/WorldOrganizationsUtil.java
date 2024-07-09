package cn.cloud9.server.reptile.worldorg;

import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

public class WorldOrganizationsUtil {
    private static final Db db = Db.use(SpringContextHolder.getBean(DataSource.class));
    private static final String url = "https://www.mfa.gov.cn/web/gjhdq_676201/gjhdqzz_681964/";


    @SneakyThrows
    public static void start() {
        String sql =
            "CREATE TABLE `world_org` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织名称',\n" +
            "  `link` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接地址',\n" +
            "  `detail` text COLLATE utf8mb4_general_ci COMMENT '详细',\n" +
            "  `gen_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='世界组织机构';";
        db.execute(sql);

        /* 解析文档 */
        final String html = HttpUtil.get(url);
        final Document document = Jsoup.parse(html);
        final Element container = document.getElementsByClass("linkList2").get(0);

        container.getElementsByTag("a").forEach(aTag -> {

            final String ORG_NAME = aTag.text();
            final String href = aTag.attr("href");
            final String replace = href.replace("./", "");
            final String redirectHtml = HttpUtil.get(url + replace);
            final Document redirectDoc = Jsoup.parse(redirectHtml);

            final String data = redirectDoc
                    .getElementsByTag("script")
                    .stream()
                    .filter(script -> script.data().contains("window.location.href"))
                    .findFirst()
                    .get()
                    .data();

            final String targetHref = data.substring(data.indexOf("./"), data.length() - 3);
            String TH = url + replace + targetHref;
            final String detailCode = HttpUtil.get(TH);
            final Document detailDoc = Jsoup.parse(detailCode);
            final Element content = detailDoc.getElementById("content");
            if (null == content) return;
            final String text = content.text();
            try {
                db.insert(Entity
                        .create("world_org")
                        .set("name", ORG_NAME)
                        .set("link", TH)
                        .set("detail", text)
                        .set("gen_time", LocalDateTime.now()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        db.execute("RENAME TABLE `world_org` TO `world_org_" + new Date().getTime() + "`");
    }
}
