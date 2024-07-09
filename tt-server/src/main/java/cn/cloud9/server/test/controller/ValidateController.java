package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.validator.groups.IdCheck;
import cn.cloud9.server.test.model.TestModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description JSR-303校验测试类
 * @project tt-server
 * @date 2022年11月08日 下午 08:31
 */
@RestController
@RequestMapping("/test/valid")
public class ValidateController {


    /**
     *
     * {
     *     "email": "1791255334@163.com",
     *     "account": "1791255334",
     *     "labelName": "标签名称",
     *     "amount": 100,
     *     "id": 1001
     * }
     *
     * @Validated 开启参数校验，如果参数校验失败，将抛出 MethodArgumentNotValidException 异常。
     * /test/base/validator
     * 测试JSR-303校验组件
     * @param model
     * @return
     */
    @PostMapping("/normal")
    public String hibernateValidatorTest(@Validated @RequestBody TestModel model) {
        return "校验成功！" + model.toString();
    }

    /**
     * 分组校验测试
     * @param model
     * @return
     */
    @PostMapping("/group")
    public String hibernateValidatorTest2(@Validated(value = IdCheck.class) @RequestBody TestModel model) {
        return "分组校验成功！" + model.toString();
    }
}
