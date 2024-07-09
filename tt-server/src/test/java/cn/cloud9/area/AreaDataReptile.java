package cn.cloud9.area;

import cn.cloud9.server.MainApplication;
import cn.cloud9.server.system.common.area.dto.AreaDTO;
import cn.cloud9.server.system.common.area.mapper.AreaMapper;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author OnCloud9
 * @description 行政区域三级数据爬取工具类
 * @project tt-server
 * @date 2022年11月19日 下午 07:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AreaDataReptile {
    public static final String PROVINCE_CODE = "provinceCode";
    public static final String PROVINCE_NAME = "provinceName";
    public static final String CITY_CODE = "cityCode";
    public static final String CITY_NAME = "cityName";
    public static final String COUNTY_CODE = "countyCode";
    public static final String COUNTY_NAME = "countyName";
    public static final String TOWN_CODE = "townCode";
    public static final String TOWN_NAME = "townName";
    public static final String VILLAGE_CODE = "villageCode";
    public static final String VILLAGE_NAME = "villageName";


    // 我是按照博客园的文章 用Java写的一个爬虫程序， 这里选了2020年的数据页
    public static final String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/";
    private static final Map<String, String> headers = new HashMap<>();

    @Resource
    private AreaMapper areaMapper;

    @Test
    public void reptileStart() {
        // 31个省 + 自治区 + 直辖市
        final List<Map<String, Object>> provinceList = new ArrayList<>();
        getProvinceList(url, provinceList);

        // 342个 市 + 直辖市区 + 自治区县
        final List<Map<String, Object>> cityList = new ArrayList<>();
        provinceList.forEach(map -> {
            String url = String.valueOf(map.get("link"));
            if (null == url) return;
            getCityList(url, cityList, map);
        });

        // 3271个区县
        final List<Map<String, Object>> countyList = new LinkedList<>();
        cityList.forEach(map -> {
            String url = String.valueOf(map.get("link"));
            if (null == url) return;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getCountyList(url, countyList, map);
        });
    }

    public void getCountyList(String url, List<Map<String, Object>> countyList, Map<String, Object> city) {
        String htmlSourceCode =  HttpUtil.get(url);
        Document document = Jsoup.parse(htmlSourceCode);
        Elements countytr = document.getElementsByClass("countytr");
        countytr.forEach(element -> {
            // 这一级开始存在叶子节点了
            Elements tdTags = element.getElementsByTag("td");

            String tdTag1 = tdTags.get(0).html();
            if (tdTag1.contains("href")) {
                Elements aTags = element.getElementsByTag("a");
                Element aTag1 = aTags.get(0);
                Element aTag2 = aTags.get(1);
                String href = aTag1.attr("href");
                String countyCode = aTag1.text();
                String countyName = aTag2.text();

                Map<String, Object> county = new HashMap<>();
                county.put("countyName", countyName);
                county.put("countyCode", countyCode);
                county.put("link",  AreaDataReptile.url + href.split("/")[1].substring(0,2) + "/" + href);

                final AreaDTO cityDTO = (AreaDTO)city.get("cityDTO");
                final String path = cityDTO.getAreaPath();

                final AreaDTO areaDTO = new AreaDTO();
                areaDTO.setId(Long.valueOf(countyCode));
                areaDTO.setAreaName(countyName);
                areaDTO.setAreaLevel(3);
                areaDTO.setAreaPath(path + "," + countyCode);
                areaDTO.setParentId(cityDTO.getId());
                areaDTO.setAreaUrl(AreaDataReptile.url + href.split("/")[1].substring(0,2) + "/" + href);
                areaDTO.setCreateTime(LocalDateTime.now());
                areaMapper.insert(areaDTO);

                countyList.add(county);

                System.out.printf("(%s, '%s', '%s', 3, NULL, '%s', NOW()),\n", countyCode, countyName, city.get(CITY_CODE), county.get("link"));
                // System.out.println(county);
            } else  {

                String countyName = tdTags.get(1).text();

                Map<String, Object> county = new HashMap<>();
                county.put("countyName", countyName);
                county.put("countyCode", tdTags.get(0).text());
                county.put("link", null);
                countyList.add(county);

                final AreaDTO cityDTO = (AreaDTO)city.get("cityDTO");
                final String path = cityDTO.getAreaPath();

                final AreaDTO areaDTO = new AreaDTO();
                areaDTO.setId(Long.valueOf(tdTags.get(0).text()));
                areaDTO.setAreaName(countyName);
                areaDTO.setAreaLevel(3);
                areaDTO.setAreaPath(path + "," + tdTags.get(0).text());
                areaDTO.setParentId(cityDTO.getId());
                areaDTO.setCreateTime(LocalDateTime.now());
                areaMapper.insert(areaDTO);

                System.out.printf("(%s, '%s', '%s', 3, NULL, NULL, NOW()),\n", tdTags.get(0).text(), countyName, city.get(CITY_CODE) );
                // System.out.println(county);
            }
        });
    }


    private void getCityList(String s, List<Map<String, Object>> cityList, Map<String, Object> province) {
        String currentUrl = AreaDataReptile.url + s;
        String htmlSourceCode = HttpUtil.get(currentUrl);
        Document document = Jsoup.parse(htmlSourceCode);
        Elements citytr = document.getElementsByClass("citytr");
        citytr.forEach(element -> {
            Elements tdTags = element.getElementsByTag("td");

            String tdTag1 = tdTags.get(0).html();
            if (tdTag1.contains("href")) {
                Elements aTags = element.getElementsByTag("a");
                Element aTag1 = aTags.get(0);
                Element aTag2 = aTags.get(1);
                String href = aTag1.attr("href");
                String cityCode = aTag1.text();
                String cityName = aTag2.text();

                Map<String, Object> city = new HashMap<>();
                city.put("cityName", cityName);
                city.put("cityCode", cityCode);
                city.put("link", AreaDataReptile.url + href);

                final AreaDTO areaDTO = new AreaDTO();
                areaDTO.setId(Long.valueOf(cityCode));
                areaDTO.setAreaName(cityName);
                areaDTO.setAreaLevel(2);
                areaDTO.setAreaPath(province.get(PROVINCE_CODE) + "," + cityCode);
                areaDTO.setParentId(Long.valueOf(String.valueOf(province.get(PROVINCE_CODE))));
                areaDTO.setAreaUrl(AreaDataReptile.url + href);
                areaDTO.setCreateTime(LocalDateTime.now());
                areaMapper.insert(areaDTO);

                city.put("cityDTO", areaDTO);

                cityList.add(city);
                System.out.printf("(%s, '%s', '%s', 2, NULL, '%s', NOW()),\n", cityCode, cityName, province.get(PROVINCE_CODE), city.get("link"));
            } else  {

                String cityName = tdTags.get(1).text();

                Map<String, Object> city = new HashMap<>();
                city.put("cityName", cityName);
                city.put("cityCode", tdTags.get(0).text());
                city.put("link", null);

                final AreaDTO areaDTO = new AreaDTO();
                areaDTO.setId(Long.valueOf(tdTags.get(0).text()));
                areaDTO.setAreaName(cityName);
                areaDTO.setAreaLevel(2);
                areaDTO.setParentId(Long.valueOf(String.valueOf(province.get(PROVINCE_CODE))));
                areaDTO.setAreaPath(province.get(PROVINCE_CODE) + "," + tdTags.get(0).text());
                areaDTO.setCreateTime(LocalDateTime.now());
                areaMapper.insert(areaDTO);

                city.put("cityDTO", areaDTO);

                cityList.add(city);
                System.out.printf("(%s, '%s', '%s', 2, NULL, NULL, NOW()),\n", tdTags.get(0).text(), cityName, province.get(PROVINCE_CODE) );

            }
        });
    }


    private void getProvinceList(String url, List<Map<String, Object>> provinceList) {

        // 请求得到网页源代码字符串（网页格式简单，可以直接找）
        String htmlSourceCode = HttpUtil.get(url);

        // 用JSOUP解析成文档对象
        Document document = Jsoup.parse(htmlSourceCode);
        // 省级的是用这个class写的
        Elements provincetr = document.getElementsByClass("provincetr");

        provincetr.forEach(element -> {

            // 下面直接找a标签就行了
            Elements aTags = element.getElementsByTag("a");
            for (Element aTag : aTags) {
                // <a href="11.html">北京市</a> 这样的结构
                String href = aTag.attr("href"); //
                String text = aTag.text();

                // 装填需要的信息
                Map<String, Object> province = new HashMap<>();
                province.put(PROVINCE_NAME, text);
                province.put(PROVINCE_CODE, href.split("\\.")[0] + "0000000000");
                province.put("link", href);

                final AreaDTO areaDTO = new AreaDTO();
                areaDTO.setId(Long.valueOf(href.split("\\.")[0] + "0000000000"));
                areaDTO.setAreaName(text);
                areaDTO.setAreaLevel(1);
                areaDTO.setParentId(0L);
                areaDTO.setAreaUrl(url + href);
                areaDTO.setAreaPath(href.split("\\.")[0] + "0000000000");
                areaDTO.setCreateTime(LocalDateTime.now());
                areaMapper.insert(areaDTO);

                province.put("provinceDTO", areaDTO);

                /* System.out.printf("(%s, '%s', '%s', 1, NULL, '%s', NOW()),\n", province.get(PROVINCE_CODE), text, "0", url + href); */
                provinceList.add(province);
            }
        });
    }
}
