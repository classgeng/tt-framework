package cn.cloud9.reflect;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 财务凭证参数
 * @author 戴知舟
 * @version 1.0
 * @project amerp-server
 * @date 2023年03月21日 09:37
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class KdVoucherDTO {

    @JSONField(name = "Model")
    private Model model;

    /**
     * 提交必传值
     */
    @JSONField(name = "Numbers")
    private Integer[] numbers;

    @Builder
    @Accessors(chain = true)
    @Data
    public static final class Model {
        /* 凭证编号 */
        @JSONField(name = "FBillNo")
        private Integer fBillNo;

        /* 凭证ID？ */
        @JSONField(name = "FVOUCHERID")
        private Integer fVoucherId;

        /* 账簿：FAccountBookID  (必填项) “ZB”<取公司编码右4位> */
        @JSONField(name = "FAccountBookID")
        private FAccountBookID fAccountBookID;

        /* 日期：FDate(必填项) 取“凭证生成日期” 2023-03-20 年-月-日 */
        @JSONField(name = "FDate", format = "yyyy-MM-dd")
        private Date fDate;

        /* 凭证字：FVOUCHERGROUPID  (必填项) 常量“系统”/“PZZ1” */
        @JSONField(name = "FVOUCHERGROUPID")
        private FVoucherGroupId fVoucherGroupId;

        /* 附件数：FATTACHMENTS 销明细中发票数量合计 */
        @JSONField(name = "FATTACHMENTS")
        private Integer fAttachments;

        /* 会计年度：FYEAR 2023 */
        @JSONField(name = "FYEAR")
        private Integer fYear;

        /* 期间：FPERIOD */
        @JSONField(name = "FPERIOD")
        private Integer fPeriod;

        /* 借方总金额：FDEBITTOTAL 	取报销单中的合计金额 */
        @JSONField(name = "FDEBITTOTAL")
        private BigDecimal fDebitTotal;

        /* 贷方总金额：FCREDITTOTAL	取报销单中的合计金额 */
        @JSONField(name = "FCREDITTOTAL")
        private BigDecimal fCreditTotal;

        /* 审核状态：FDocumentStatus  (必填项) */
        @JSONField(name = "FDocumentStatus")
        private String fDocumentStatus;

        /*  制单：FCreatorId 员工编号 */
        // @JSONField(name = "FCreatorId")
        // private String fCreatorId;

        /* 凭证号：FVOUCHERGROUPNO  (必填项) */
        @JSONField(name = "FVOUCHERGROUPNO")
        private Integer fVoucherGroupNo;

        /* 单据体：FEntity */
        @JSONField(name = "FEntity")
        private List<FEntity> fEntities;
        @Builder
        @Accessors(chain = true)
        @Data
        public static final class FAccountBookID {
            @JSONField(name = "FNumber")
            private String fNumber;
        }
        @Builder
        @Accessors(chain = true)
        @Data
        public static final class FVoucherGroupId {
            @JSONField(name = "FNumber")
            private String fNumber;
        }

        /**
         * @author 戴知舟
         * @date 2023/3/21 11:17
         * @description 借方实体类
         */
        @Builder
        @Accessors(chain = true)
        @Data
        public static final class FEntity {
            /* 摘要：FEXPLANATION	采用<单据编号>|<报销人>报销<往来对象简称>的<将报销明细中费用类型“-”前的内容去重拼接> */
            @JSONField(name = "FEXPLANATION")
            private String fExplanation;

            /* 科目编码：FACCOUNTID  (必填项) 单据中的“会计科目”编码 sys_ar_su_id -> su_code */
            @JSONField(name = "FACCOUNTID")
            private FAccountId fAccountId;

            /* 核算维度：FDetailID */
            @JSONField(name = "FDetailID")
            private FDetailId fDetailID;

            /* 币别：FCURRENCYID(必填项)	暂使用常量“PRE001” */
            @JSONField(name = "FCURRENCYID")
            private FCurrencyId fCurrencyId;

            /* 汇率类型：FEXCHANGERATETYPE(必填项)	暂使用常量“HLTX01_SYS” */
            @JSONField(name = "FEXCHANGERATETYPE")
            private FExchangeRateType fExchangeRateType;

            /*  汇率：FEXCHANGERATE	暂使用常量“1” */
            @JSONField(name = "FEXCHANGERATE")
            private Integer FExchangeRate;

            /*  原币金额：FAMOUNTFOR	取对应用款单表单明细表中的金额 */
            @JSONField(name = "FAMOUNTFOR")
            private BigDecimal fAmountFor;

            /*  借方金额：FDEBIT	取对应用款单表单明细表中的金额 */
            @JSONField(name = "FDEBIT")
            private BigDecimal fDebit;

            /*  贷方金额：FCREDIT	取对应用款单表单明细表中的金额 */
            @JSONField(name = "FCREDIT")
            private BigDecimal fCredit;

            @Builder
            @Accessors(chain = true)
            @Data
            public static final class FAccountId {
                @JSONField(name = "FNumber")
                private String fNumber;
            }
            @Builder
            @Accessors(chain = true)
            @Data
            public static final class FCurrencyId {
                @JSONField(name = "FNumber")
                private String fNumber;
            }
            @Builder
            @Accessors(chain = true)
            @Data
            public static final class FExchangeRateType {
                @JSONField(name = "FNumber")
                private String fNumber;
            }
            @Builder
            @Accessors(chain = true)
            @Data
            public static final class FDetailId {
                /* 部门：FDETAILID__FFLEX5	取单据中的“申请部门”编码 sys_ar_de_id -> de_code */
                @JSONField(name = "FDETAILID__FFLEX5")
                private FDetailIdFflex5 fDetailIdFflex5;

                /*  客户：FDETAILID__FFLEX6	取单据中的“往来对象”编码 cu_id || em_id -> cu_code || em_code  */
                @JSONField(name = "FDETAILID__FFLEX6")
                private FDetailIdFflex6 fDetailIdFflex6;

                /* 员工：FDETAILID__FFLEX7	取单据中的“报销人”编码 apProposer -> em_code */
                @JSONField(name = "FDETAILID__FFLEX7")
                private FDetailIdFflex7 fDetailIdFflex7;

                /* 项目档案：FDETAILID__FF100002	取报销明细中“项目”编码 项目id -> in_code */
                @JSONField(name = "FDETAILID__FF100002")
                private FDetailIdFF100002 fDetailIdFF100002;

                @Builder
                @Accessors(chain = true)
                @Data
                public static final class FDetailIdFflex5 {
                    @JSONField(name = "FNumber")
                    private String fNumber;
                }

                @Builder
                @Accessors(chain = true)
                @Data
                public static final class FDetailIdFflex6 {
                    @JSONField(name = "FNumber")
                    private String fNumber;
                }

                @Builder
                @Accessors(chain = true)
                @Data
                public static final class FDetailIdFflex7 {
                    @JSONField(name = "FNumber")
                    private String fNumber;
                }

                @Builder
                @Accessors(chain = true)
                @Data
                public static final class FDetailIdFF100002 {
                    @JSONField(name = "FNumber")
                    private String fNumber;
                }
            }
        }
    }
}
