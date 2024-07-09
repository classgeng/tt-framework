package cn.cloud9.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * CREATE TABLE `sal_pr_approve` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统编码 主键 ',
 *   `sal_pr_in_id` int(11) NOT NULL COMMENT '项目档案',
 *   `ap_code` varchar(32) NOT NULL COMMENT '立项编码',
 *   `ap_in_amount` decimal(11,2) NOT NULL COMMENT '收入总额 income',
 *   `ap_co_amount` decimal(11,2) NOT NULL COMMENT '成本总额 cost',
 *   `ap_ta_amount` decimal(11,2) NOT NULL COMMENT '税金 tax',
 *   `ap_ex_amount` decimal(11,2) NOT NULL COMMENT '费用 expense',
 *   `ap_pr_amount` decimal(11,2) NOT NULL COMMENT '利润总额 profit',
 *   `ap_state` char(1) NOT NULL DEFAULT '0' COMMENT '审批状态 	0审批中 1已审批 2不通过',
 *   `ap_change_state` char(1) NOT NULL DEFAULT '0' COMMENT '变更审批状态	0审批中1已审批2不通过',
 *   `create_time` datetime NOT NULL COMMENT '创建时间',
 *   `creator` varchar(32) NOT NULL COMMENT '创建人',
 *   PRIMARY KEY (`id`) USING BTREE,
 *   UNIQUE KEY `uniq_idx_ap_code` (`ap_code`) USING BTREE COMMENT '审批单号唯一索引'
 * ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='立项审批表';
 *
 */
@Slf4j
public class CollectionTest {

    /* 修改和删除都需要剔除之前的元素 */
    private static final List<String> excludesAct = Arrays.asList("1", "2");
    private static final List<String> includesAct = Arrays.asList("0", "1");

    /**
     * 1、要解决审核通过后重新计算的问题
     * 2、查看变更审批时需要展示
     */
    @Test
    public void collectTest() {
        /* 有效，审批通过的集合 */
        List<SalPrApSellDTO> validList = null;

        /* 无效，审批中的的集合 */
        List<SalPrApSellDTO> inValidList = null;

        /* 用审批的集合对之前的集合进行过滤（删掉修改和删除的） */
        final List<String> collect = inValidList.stream().filter(x -> excludesAct.contains(x.getAsChangeAct())).map(x -> x.getAsCode()).collect(Collectors.toList());

        /* 之前的集合过滤这些被修改的和删除的就是没有变更的集合 */
        validList = validList.stream().filter(x -> !collect.contains(x.getAsCode())).collect(Collectors.toList());

        /* 再将修改的和新增的添加到 未变更的集合，作为审批的统计集合 */
        List<SalPrApSellDTO> includes = inValidList.stream().filter(x -> includesAct.contains(x.getAsChange())).collect(Collectors.toList());
        validList.addAll(includes);

        /* 计算需要的值 */
        BigDecimal tax = new BigDecimal(0);
        BigDecimal income = new BigDecimal(0);
        BigDecimal cost = new BigDecimal(0);
        BigDecimal profit = new BigDecimal(0);

        for (SalPrApSellDTO sell : validList) {
            BigDecimal asInAmount = sell.getAsInAmount();
            BigDecimal asCoAmount = sell.getAsCoAmount();

            /* 收入和成本只需要累加计算 */
            income = income.add(asInAmount);
            cost = cost.add(asCoAmount);

            /* 利润 = 收入 - 成本 */
            BigDecimal thisSellsProfit = asInAmount.subtract(asCoAmount);
            profit = profit.add(thisSellsProfit);

            /* 税率需要提供连表提供 */
            BigDecimal waTaxRate = sell.getWaTaxRate();
            BigDecimal thisSellsTax = profit.multiply(waTaxRate);
            tax = tax.add(thisSellsTax);
        }
    }
}
