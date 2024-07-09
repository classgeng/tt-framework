package cn.cloud9.server.system.common.hardware;

import cn.cloud9.server.struct.util.ListPageUtil;
import cn.cloud9.server.struct.util.OperateSystemUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2023年01月01日 下午 01:28
 */
@RestController
@RequestMapping("/sys-monitor")
public class SystemMonitorController {

    /**
     * /hardware/info
     * @return
     */
    @GetMapping("/info")
    public Map<String, Object> getHardwareInfo() {
        final Map<String, Object> info = new ConcurrentHashMap<>();
        info.put("cpuInfo", OperateSystemUtil.getCpuInfo());
        info.put("memoryInfo", OperateSystemUtil.getMemoryInfo());
        info.put("graphicsCardsInfo", OperateSystemUtil.getGraphicsCardsInfo());
        info.put("soundCardInfo", OperateSystemUtil.getSoundCardInfo());
        info.put("displayInfo", OperateSystemUtil.getDisplayInfo());
        info.put("diskStoreInfo", OperateSystemUtil.getDiskStoreInfo());
        info.put("fileSystemInfo", OperateSystemUtil.getFileSystemInfo());
        info.put("logicalVolumeGroupInfo", OperateSystemUtil.getLogicalVolumeGroupInfo());
        info.put("usbDevicesInfo", OperateSystemUtil.getUsbDevicesInfo());
        info.put("systemServiceInfo", OperateSystemUtil.getSystemServiceInfo());
        info.put("processesInfo", OperateSystemUtil.getProcessesInfo());
        info.put("ipStatistics", OperateSystemUtil.getIpStatistics());
        info.put("netWorkInterfaces", OperateSystemUtil.getNetWorkInterfaces());
        info.put("networkParamsInfo", OperateSystemUtil.getNetworkParamsInfo());
        info.put("operateSystemInfo", OperateSystemUtil.getOperateSystemInfo());
        info.put("powerSourceInfo", OperateSystemUtil.getPowerSourceInfo());
        info.put("sensorInfo", OperateSystemUtil.getSensorInfo());
        return info;
    }

    /**
     * CPU信息
     * @return
     */
    @GetMapping("/cpuInfo")
    public Map<String, Object> getCpuInfo() {
        return OperateSystemUtil.getCpuInfo();
    }

    @GetMapping("/memoryInfo")
    public Map<String, Object> getMemoryInfo() {
        return OperateSystemUtil.getMemoryInfo();
    }

    @GetMapping("/graphicsCardsInfo")
    public List<Map<String, Object>> getGraphicsCardsInfo() {
        return OperateSystemUtil.getGraphicsCardsInfo();
    }

    @GetMapping("/soundCardInfo")
    public List<Map<String, Object>> getSoundCardInfo() {
        return OperateSystemUtil.getSoundCardInfo();
    }

    @GetMapping("/getDisplayInfo")
    public List<Map<String, Object>> getDisplayInfo() {
        return OperateSystemUtil.getDisplayInfo();
    }

    @GetMapping("/fileSystemInfo")
    public Map<String, Object> getFileSystemInfo() {
        return OperateSystemUtil.getFileSystemInfo();
    }

    @GetMapping("/diskStoreInfo")
    public List<Map<String, Object>> getDiskStoreInfo() {
        return OperateSystemUtil.getDiskStoreInfo();
    }

    @GetMapping("/logicalVolumeGroupInfo")
    public List<Map<String, Object>> getLogicalVolumeGroupInfo() {
        return OperateSystemUtil.getLogicalVolumeGroupInfo();
    }

    @GetMapping("/usbDevicesInfo")
    public List<Map<String, Object>> getUsbDevicesInfo() {
        return OperateSystemUtil.getUsbDevicesInfo();
    }

    @PostMapping("/systemServicePage")
    public IPage<Map<String, Object>> getSystemServiceInfo(@RequestBody SystemServiceDTO condition) {
        List<Map<String, Object>> systemServices = OperateSystemUtil.getSystemServiceInfo();
        systemServices = systemServices.stream().filter(condition::queryWrapper).collect(Collectors.toList());
        return  ListPageUtil.returnPage(condition.getPage(), systemServices);
    }

    @GetMapping("/processesInfo")
    public Map<String, Object> getProcessesInfo() {
        return OperateSystemUtil.getProcessesInfo();
    }

    @GetMapping("/ipStatistics")
    public Map<String, Object> getIpStatistics() {
        return OperateSystemUtil.getIpStatistics();
    }

    @GetMapping("/netWorkInterfaces")
    public List<Map<String, Object>> getNetWorkInterfaces() {
        return OperateSystemUtil.getNetWorkInterfaces();
    }

    @GetMapping("/networkParamsInfo")
    public Map<String, Object> getNetworkParamsInfo() {
        return OperateSystemUtil.getNetworkParamsInfo();
    }

    @GetMapping("/operateSystemInfo")
    public Map<String, Object> getOperateSystemInfo() {
        return OperateSystemUtil.getOperateSystemInfo();
    }

    @GetMapping("/powerSourceInfo")
    public List<Map<String, Object>> getPowerSourceInfo() {
        return OperateSystemUtil.getPowerSourceInfo();
    }

    @GetMapping("/sensorInfo")
    public Map<String, Object> getSensorInfo() {
        return OperateSystemUtil.getSensorInfo();
    }

    @GetMapping("/jvmInfo")
    public Map<String, Object> getJvmInfo() {
        return OperateSystemUtil.getJvmInfo();
    }

    @GetMapping("/serverSysInfo")
    public Map<String, Object> getServerSysInfo() {
        return OperateSystemUtil.getServerSysInfo();
    }

    /**
     * 获取JVM内存情况，实时的
     * @return
     */
    @GetMapping("/jvm-memory")
    public Map<String, Object> getJvmMemory() {

        return OperateSystemUtil.getJvmMemory();
    }

    @GetMapping("/thread-info")
    public Map<String, Object> getAppThreadInfo() {
        return OperateSystemUtil.getAppThreadInfo();
    }
}
