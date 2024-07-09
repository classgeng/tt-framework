package cn.cloud9.collection;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * sal_pr_apsell 立项审批销售商品表 实体类
 *
 * @projectName:
 * @author:daizhizhou
 * @date:2023-03-24
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sal_pr_apsell")
public class SalPrApSellDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统编码 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 立项审批 引用表<立项审批>
     */
    @TableField("sal_pr_ap_id")
    private Integer salPrApId;



    /**
     * 立项审批销售商品编码 这里的编码可以重复，修改变更用原来的编码，新增变更用新的编码；
     */
    @TableField("as_code")
    private String asCode;

    /**
     * 商品档案 引用表<商品档案>
     */
    @TableField("sys_ar_wa_id")
    private Integer sysArWaId;

    /**
     * 商品分类
     */
    @TableField("as_wa_cate")
    private String asWaCate;

    @TableField(exist = false)
    private String asWaCateName;


    /**
     * 商品名称
     */
    @TableField("as_wa_name")
    private String asWaName;

    /**
     * 规格型号 specification
     */
    @TableField("as_wa_spec")
    private String asWaSpec;

    /**
     * 单位 specification
     */
    @TableField("as_wa_unit")
    private String asWaUnit;

    /**
     * 数量 specification
     */
    @TableField("as_wa_count")
    private Integer asWaCount;

    /**
     * 采购单价
     */
    @TableField("as_co_price")
    private BigDecimal asCoPrice;

    /**
     * 采购金额
     */
    @TableField("as_co_amount")
    private BigDecimal asCoAmount;

    /**
     * 销售单价
     */
    @TableField("as_in_price")
    private BigDecimal asInPrice;

    /**
     * 销售金额
     */
    @TableField("as_in_amount")
    private BigDecimal asInAmount;

    /**
     * 备注
     */
    @TableField("as_remark")
    private String asRemark;

    /**
     * 是变更记录 0原始 1变更
     */
    @TableField("as_change")
    private String asChange;

    @TableField(exist = false)
    private String asChangeName;

    /**
     * 变更操作 0新增 1修改 2删除
     */
    @TableField("as_change_act")
    private String asChangeAct;

    @TableField(exist = false)
    private String asChangeActName;

    /**
     * 0审批中 1已审批 2不通过
     */
    @TableField("as_state")
    private String asState;

    private String asStateName;

    /**
     * 是否有效 0无效 1有效
     */
    @TableField("as_valid")
    private String asValid;

    @TableField(exist = false)
    private String asValidName;

    @TableField(exist = false)
    private BigDecimal waTaxRate;

    /**
     * @author 戴知舟
     * @date 2023/3/29 10:25
     * @description 比较新提交的记录发生了哪些值的更新
     * @params [dto] 表单提交的记录
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public boolean changeCompare(SalPrApSellDTO dto) {

        boolean isSameSysArWaId = sysArWaId.equals(dto.sysArWaId);
        boolean isSameAsWaCate = asWaCate.equals(dto.asWaCate);
        boolean isSameAsWaName = asWaName.equals(dto.asWaName);
        boolean isSameAsWaSpec = asWaSpec.equals(dto.asWaSpec);
        boolean isSameAsWaUnit = asWaUnit.equals(dto.asWaUnit);
        boolean isSameAsWaCount = asWaCount.equals(dto.asWaCount);
        boolean isSameAsCoPrice = asCoPrice.equals(dto.asCoPrice);
        boolean isSameAsCoAmount = asCoAmount.equals(dto.asCoAmount);
        boolean isSameAsInPrice = asInPrice.equals(dto.asInPrice);
        boolean isSameAsInAmount = asInAmount.equals(dto.asInAmount);
        boolean isSameAsRemark = asRemark.equals(dto.asRemark);

        Boolean[] flags = new Boolean[] {
                isSameSysArWaId, isSameAsWaCate, isSameAsWaName,
                isSameAsWaSpec, isSameAsWaUnit, isSameAsWaCount,
                isSameAsCoPrice, isSameAsCoAmount,
                isSameAsInPrice, isSameAsInAmount,
                isSameAsRemark,
        };
        return Arrays.stream(flags).anyMatch(f -> f.equals(false));
    }

}
