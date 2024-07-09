package cn.cloud9.server.struct.api;

import cn.cloud9.server.struct.common.BaseController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月03日 下午 08:26
 */
@RestController
@RequestMapping("/system-api")
public class SystemApiController extends BaseController {

    @Resource
    private SystemApiService systemApiService;

    @PostMapping("/page")
    public IPage<SystemApiDTO> getSystemApiPage(@RequestBody SystemApiDTO dto) {
        return systemApiService.getSystemApiPage(dto);
    }

}
