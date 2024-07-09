package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.exception.ServiceException;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.log.SystemLogDAO;
import cn.cloud9.server.struct.log.SystemLogDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description 日志切点测试
 * @project tt-server
 * @date 2022年11月08日 下午 08:31
 */
@RestController
@RequestMapping("/test/aspect")
public class LogAspectController {

    @Resource
    private SystemLogDAO systemLogDAO;
    /**
     * 测试日志外切配置效果，正常时返回包裹的结果
     * @return
     */
    @SystemLog(module = "测试", businessType = BusinessType.QUERY)
    @GetMapping("/point")
    public String pointCutTest() {
        return "注解切面测试";
    }

    /**
     * 测试日志外切配置效果，当异常时返回什么内容
     * @return
     */
    @SystemLog(module = "测试2", businessType = BusinessType.QUERY)
    @GetMapping("/point2")
    public String pointCutTest2() {
        throw new ServiceException(ResultMessage.ALREADY_EXIST, "xxxx");
    }


    @GetMapping("/page")
    public IPage<SystemLogDTO> getLogPage(@RequestBody SystemLogDTO dto) {
        final LambdaQueryWrapper<SystemLogDTO> wrapper = Wrappers.lambdaQuery();
        return systemLogDAO.selectPage(dto.getPage(), wrapper);
    }
}
