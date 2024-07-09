package cn.cloud9.server.struct.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 淘宝API：http://ip.taobao.com/service/getIpInfo.php?ip=218.192.3.42
 * 新浪API：http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=218.192.3.42
 * pconline API：http://whois.pconline.com.cn/
 * 百度API：http://api.map.baidu.com/location/ip?ip=218.192.3.42
 *
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月08日 下午 09:48
 */
public class AddressUtil {
    private static final Logger log = LoggerFactory.getLogger(AddressUtil.class);

    /**
     * 调用淘宝的接口
     */
    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getRealAddressByIP(String ip) {
        String address = "XX XX";
        /* 内网不查询 */
        if (IpUtil.internalIp(ip)) return "内网IP";

        String rspStr = HttpUtil.sendPost(IP_URL, "ip=" + ip);
        if (StringUtils.isEmpty(rspStr)) {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        JSONObject obj = JSONObject.parseObject(rspStr);
        JSONObject data = obj.getObject("data", JSONObject.class);
        String region = data.getString("region");
        String city = data.getString("city");
        address = region + " " + city;
        return address;
    }

    public static void main(String[] args) {
        String ip = "218.192.3.42";
        System.out.println(getRealAddressByIP(ip));
    }
}
