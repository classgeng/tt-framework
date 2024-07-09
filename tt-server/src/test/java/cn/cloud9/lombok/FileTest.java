package cn.cloud9.lombok;

import cn.cloud9.server.struct.file.FileUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月15日 下午 11:31
 */
public class FileTest {
    @Test
    public void dirCreateTest() {
        final String path = FileUtil.createDirPathByYearMonth();
        System.out.println(path);

        final String s = FileUtil.getFileNameWithoutTypeSuffix("sss.jpg");
        System.out.println(s);

        final String s1 = "aaa-111";
        System.out.println(s1.substring(s1.lastIndexOf("-") + 1));

        List<String> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a.add("" + i);
        }

        a.sort((a1, b1) -> Integer.valueOf(b1).compareTo(Integer.valueOf(a1)));
        System.out.println(a);

    }
}
