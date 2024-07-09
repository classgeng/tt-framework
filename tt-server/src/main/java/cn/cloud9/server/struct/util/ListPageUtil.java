package cn.cloud9.server.struct.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description List集合 翻页工具类
 * @project tt-server
 * @date 2022年12月03日 下午 11:41
 */
public class ListPageUtil {


    /**
     * 对集合数据进行翻页
     * @param page 翻页对象
     * @param totalList 总的集合
     * @param <Model> 模型实体
     * @return 翻页后的翻页对象
     */
    public static <Model> IPage<Model> returnPage(Page<Model> page, List<Model> totalList) {
        /* 当前页下标 */
        final long pageCurrent = page.getCurrent();
        /* 每页大小 */
        final long pageSize = page.getSize();
        /* 总记录数 */
        final int total = totalList.size();
        page.setTotal(total);

        /* 总页数 */
        final Long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;

        /* 起始下标 */
        final Long beginIdx = (pageCurrent - 1L) * pageSize;

        /* 偏移下标 */
        final Long offsetIdx = beginIdx + pageSize;

        boolean isEmptyRecord = CollectionUtils.isEmpty(totalList);
        boolean isExceedPage = pageCurrent > totalPage;
        boolean isWrongParam = pageCurrent < 1 || pageSize < 1;
        if (isExceedPage || isWrongParam || isEmptyRecord) {
            page.setRecords(Collections.emptyList());
            return page;
        }

        final List<Model> models = totalList.stream().skip(beginIdx).limit(pageSize).collect(Collectors.toList());
        page.setRecords(models);
        page.setPages(totalPage);

        return page;
    }

}
