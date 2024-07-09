package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.masking.annotation.ActiveDataMasking;
import cn.cloud9.server.test.model.MaskingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description 数据脱敏测试类
 * @project tt-server
 * @date 2022年11月27日 上午 11:35
 */
@ActiveDataMasking
@RestController
@RequestMapping("/test/data-mask")
public class MaskingController {

    /**
     * 测试接口样例
     * @return
     */
    @GetMapping("/demo")
    public MaskingModel getMaskingData() {
        final MaskingModel maskingModel = new MaskingModel();
        maskingModel.setIdCard("362202198708064434");
        return maskingModel;
    }


}
