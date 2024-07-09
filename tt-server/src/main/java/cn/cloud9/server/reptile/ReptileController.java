package cn.cloud9.server.reptile;

import cn.cloud9.server.reptile.carhome.CarHomeUtil;
import cn.cloud9.server.reptile.kfc.KfcUtil;
import cn.cloud9.server.reptile.nations.WorldNationsUtil;
import cn.cloud9.server.reptile.worldorg.WorldOrganizationsUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reptile")
public class ReptileController {


    @GetMapping("/kfc/start")
    public void startKfcReptile() {
        new Thread(KfcUtil::start).start();
    }

    @GetMapping("/car-series/start")
    public void startCarSeriesReptile() {
        new Thread(CarHomeUtil::start).start();
    }

    @GetMapping("/world-org/start")
    public void startWorldOrganizationsReptile() {
        new Thread(WorldOrganizationsUtil::start).start();
    }

    @GetMapping("/world-nation/start")
    public void startWorldNationsReptile() {
        new Thread(WorldNationsUtil::start).start();
    }
}
