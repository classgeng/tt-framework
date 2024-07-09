package cn.cloud9.server.test.model;

import cn.cloud9.server.struct.masking.annotation.MaskingField;
import cn.cloud9.server.struct.masking.enums.MaskingType;
import cn.cloud9.server.struct.masking.intf.impl.TestCustomMasking;
import lombok.Data;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月27日 上午 11:28
 */
@Data
public class MaskingModel {

    private String idCard;

    @MaskingField(srcField = "idCard", maskingType = MaskingType.ID_CARD)
    private String maskedIdCard;

    @MaskingField(srcField = "idCard", usingCustom = true, custom = TestCustomMasking.class)
    private String customMaskedIdCard;
}
