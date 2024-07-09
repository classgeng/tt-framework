package cn.cloud9.oshi;

import cn.cloud9.server.struct.util.OperateSystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月31日 上午 10:11
 */
@Slf4j
public class HardWareInfoTest {

    @Test
    public void systemInfo() {
        final PlatformEnum currentPlatform = SystemInfo.getCurrentPlatform();
        final String platformName = currentPlatform.getName();
        log.info("PlatformEnum.toString {}", currentPlatform);

        log.info("currentPlatform 当前平台  : {}", platformName);


        final SystemInfo systemInfo = new SystemInfo();
        log.info("SystemInfo.toString {}", systemInfo);

        final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        final String family = operatingSystem.getFamily();
        final String manufacturer = operatingSystem.getManufacturer();
        final OperatingSystem.OSVersionInfo versionInfo = operatingSystem.getVersionInfo();
        final String version = versionInfo.getVersion();
        final String buildNumber = versionInfo.getBuildNumber();
        final String codeName = versionInfo.getCodeName();

        log.info("操作系统 : {} {} {} {} {}", manufacturer, family, version, buildNumber, codeName);

        final int processCount = operatingSystem.getProcessCount();
        log.info("进程数量：{}", processCount);

        final int threadCount = operatingSystem.getThreadCount();
        log.info("线程数量：{}", threadCount);

        final HardwareAbstractionLayer hardware = systemInfo.getHardware();

        /* 电源信息 */
        final List<PowerSource> powerSources = hardware.getPowerSources();
        for (PowerSource powerSource : powerSources) {
            log.info("powerSource.toString： {}", powerSource);
            final String pm = powerSource.getManufacturer();
            final String powerSourceName = powerSource.getName();
            final double amperage = powerSource.getAmperage();
            final PowerSource.CapacityUnits capacityUnits = powerSource.getCapacityUnits();
            final int currentCapacity = powerSource.getCurrentCapacity();
            final String deviceName = powerSource.getDeviceName();
            final String serialNumber = powerSource.getSerialNumber();

            final double voltage = powerSource.getVoltage();
            log.info("电源厂商： {}", pm);
            log.info("电源名称： {}", powerSourceName);
            log.info("电源的电池安培数，以毫安(mA)为单位。 {}", amperage);
            log.info("电源 容量单位 {}", capacityUnits);
            log.info("电源 当前容量 {}", currentCapacity);
            log.info("电源 设备名 {}", deviceName);
            log.info("电源 序列号 {}", serialNumber);
            log.info("电源 电压 {}", voltage);
        }

        /* CPU信息对象 */
        final CentralProcessor processor = hardware.getProcessor();
        log.info("CentralProcessor {}", processor);
        log.info("物理核数 {}", processor.getPhysicalProcessorCount());
        log.info("逻辑核数 {}", processor.getLogicalProcessorCount());
        log.info("物理包数 {}", processor.getPhysicalPackageCount());
        for (CentralProcessor.ProcessorCache processorCache : processor.getProcessorCaches()) {
            log.info("processorCache {}", processorCache);
        }

        /* CPU识别信息对象 */
        final CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        log.info("CentralProcessor.ProcessorIdentifier.toString {}", processorIdentifier);
        log.info("CPU 家族 {}", processorIdentifier.getFamily());
        log.info("CPU 身份标识 {}", processorIdentifier.getIdentifier());
        log.info("CPU 名称 {}", processorIdentifier.getName());
        log.info("CPU 型号 {}", processorIdentifier.getModel());
        log.info("CPU 供应商 {}", processorIdentifier.getVendor());
        log.info("CPU 微架构？ {}", processorIdentifier.getMicroarchitecture());
        log.info("CPU 步进？ {}", processorIdentifier.getStepping());
        log.info("CPU 是否为64位 {}", processorIdentifier.isCpu64bit());
        log.info("CPU 供应商频率？ {}", processorIdentifier.getVendorFreq());
        log.info("CPU 处理器ID？ {}", processorIdentifier.getProcessorID());


        /* 内存信息对象 */
        final GlobalMemory globalMemory = hardware.getMemory();
        log.info("globalMemory.toString {}", globalMemory);
        final long memoryTotal = globalMemory.getTotal();
        log.info("总共内存 {}", memoryTotal);

        final long available = globalMemory.getAvailable();
        log.info("可用内存 {}", available);

        final long pageSize = globalMemory.getPageSize();
        log.info("内存页数（内存页中的字节数）？ {}", pageSize);

        final VirtualMemory virtualMemory = globalMemory.getVirtualMemory();
        log.info("virtualMemory.toString {}", virtualMemory);
        final long swapTotal = virtualMemory.getSwapTotal();
        log.info("虚拟内存总交换总数 {}", swapTotal);

        final long swapUsed = virtualMemory.getSwapUsed();
        log.info("虚拟内存交换使用数 {}", swapUsed);

        final List<PhysicalMemory> physicalMemorys = globalMemory.getPhysicalMemory();
        for (PhysicalMemory physicalMemory : physicalMemorys) {
            log.info("physicalMemory.toString {}", physicalMemory);
            final String bankLabel = physicalMemory.getBankLabel();
            final String physicalMemoryManufacturer = physicalMemory.getManufacturer();
            final String memoryType = physicalMemory.getMemoryType();
            final long capacity = physicalMemory.getCapacity();
            final long clockSpeed = physicalMemory.getClockSpeed();
            log.info("物理内存 bankLabel {}", bankLabel);
            log.info("物理内存厂商 {}", physicalMemoryManufacturer);
            log.info("物理内存类型 {}", memoryType);
            log.info("物理内存大小 {}", capacity);
            log.info("物理内存频率 {}", clockSpeed);
        }

        /* 显卡信息对象 */
        final List<GraphicsCard> graphicsCards = hardware.getGraphicsCards();
        for (GraphicsCard graphicsCard : graphicsCards) {
            final String graphicsCardName = graphicsCard.getName();
            log.info("显卡名称 {}", graphicsCardName);
            log.info("显卡版本 {}", graphicsCard.getVersionInfo());
            log.info("显卡供应商 {}", graphicsCard.getVendor());
            log.info("显卡设备ID {}", graphicsCard.getDeviceId());
            log.info("显卡 显存 {}", graphicsCard.getVRam());
        }

        final List<UsbDevice> usbDevices = hardware.getUsbDevices(true);
        for (UsbDevice usbDevice : usbDevices) {
            log.info("usb设备名称 {}", usbDevice.getName());
            log.info("usb设备序列号 {}", usbDevice.getSerialNumber());
            log.info("usb设备 唯一Id {}", usbDevice.getUniqueDeviceId());
            log.info("usb设备 产品Id {}", usbDevice.getProductId());
            log.info("usb设备 供应商 {}", usbDevice.getVendor());
            log.info("usb设备 供应商Id {}", usbDevice.getVendorId());

            final List<UsbDevice> connectedDevices = usbDevice.getConnectedDevices();
            log.info("usb设备 连接的设备数 {}", connectedDevices);

            for (UsbDevice connectedDevice : connectedDevices) {
                log.info("连接的usb设备名称 {}", connectedDevice.getName());
                log.info("连接的usb设备序列号 {}", connectedDevice.getSerialNumber());
                log.info("连接的usb设备 唯一Id {}", connectedDevice.getUniqueDeviceId());
                log.info("连接的usb设备 产品Id {}", connectedDevice.getProductId());
                log.info("连接的usb设备 供应商 {}", connectedDevice.getVendor());
                log.info("连接的usb设备 供应商Id {}", connectedDevice.getVendorId());
            }
        }


        /* 显示器信息对象？ */
        final List<Display> displays = hardware.getDisplays();
        for (Display display : displays) {
            final byte[] edid = display.getEdid();
            log.info("显示器？ edid {}", edid);
        }

        /* 计算机系统对象 */
        final ComputerSystem computerSystem = hardware.getComputerSystem();

        final String csM = computerSystem.getManufacturer();
        final String serialNumber = computerSystem.getSerialNumber();
        final String hardwareUUID = computerSystem.getHardwareUUID();
        final String model = computerSystem.getModel();
        log.info("计算机系统型号： {}", model);
        log.info("计算机系统硬件UUID： {}", hardwareUUID);
        log.info("计算机系统序列号： {}", serialNumber);
        log.info("计算机系统厂商： {}", csM);

        /* 固件信息对象 */
        final Firmware firmware = computerSystem.getFirmware();
        log.info("固件名称： {}", firmware.getName());
        log.info("固件厂商： {}", firmware.getManufacturer());
        log.info("固件版本： {}", firmware.getVersion());
        log.info("固件发布日期： {}", firmware.getReleaseDate());
        log.info("固件描述： {}", firmware.getDescription());

        /* 主板信息对象 */
        final Baseboard baseboard = computerSystem.getBaseboard();
        log.info(" baseboard.toString {}", baseboard);
        log.info("主板厂商: {}", baseboard.getManufacturer());
        log.info("主板型号: {}", baseboard.getModel());
        log.info("主板序列号: {}", baseboard.getSerialNumber());
        log.info("主板版本: {}", baseboard.getVersion());


        /* 传感器设备对象 */
        final Sensors sensors = hardware.getSensors();
        log.info(" sensors.toString {}", sensors);
        final double cpuTemperature = sensors.getCpuTemperature();
        log.info("CPU温度：{}", cpuTemperature);
        log.info("风扇速度：{}", sensors.getFanSpeeds());
        log.info("CPU电压：{}", sensors.getCpuVoltage());

        final List<SoundCard> soundCards = hardware.getSoundCards();
        for (SoundCard soundCard : soundCards) {
            log.info("---------------- 声卡设备：----------------");
            log.info(" soundCard.toString {}", soundCard);
            log.info("声卡设备：{}", soundCard.getName());
            log.info("声卡编码：{}", soundCard.getCodec());
            log.info("声卡驱动信息：{}", soundCard.getDriverVersion());
        }


        for (HWDiskStore diskStore : hardware.getDiskStores()) {
            log.info("---------------- 磁盘存储：----------------");
            log.info(" diskStore.toString {}", diskStore);
            log.info("磁盘存储名称： {}", diskStore.getName());
            log.info("磁盘存储型号： {}", diskStore.getModel());
            log.info("磁盘存储序列： {}", diskStore.getSerial());
            log.info("磁盘存储大小： {}", diskStore.getSize());
            log.info("磁盘存储 写： {}", diskStore.getWrites());
            log.info("磁盘存储 写（字节）： {}", diskStore.getWriteBytes());
            log.info("磁盘存储 读： {}", diskStore.getReads());
            log.info("磁盘存储 读（字节） ： {}", diskStore.getReadBytes());
            log.info("磁盘存储 当前队列长度： {}", diskStore.getCurrentQueueLength());

            for (HWPartition partition : diskStore.getPartitions()) {
                log.info("磁盘存储分区名： {}", partition.getName());
                log.info("磁盘存储分区标识： {}", partition.getIdentification());
                log.info("磁盘存储分区大小： {}", partition.getSize());
                log.info("磁盘存储分区 主分区？： {}", partition.getMajor());
                log.info("磁盘存储分区 类型？： {}", partition.getType());
                log.info("磁盘存储分区 uuid？： {}", partition.getUuid());
            }
        }

        final List<NetworkIF> networkIFs = hardware.getNetworkIFs();
        for (NetworkIF networkIF : networkIFs) {
            log.info("- - - - networkIF - - - -");
            log.info(" networkIF.toString {}", networkIF);
            log.info("mac地址 {}",   networkIF.getMacaddr());
            log.info("ipv4地址 {}", Arrays.asList(networkIF.getIPv4addr()));
            log.info("ipv6地址 {}",   Arrays.asList(networkIF.getIPv6addr()));
            log.info("子网掩码 {}",   Arrays.asList(networkIF.getSubnetMasks()));
            log.info("网络名称？ {}",   networkIF.getName());
            log.info("网络展示名称？ {}",   networkIF.getDisplayName());
            log.info("接口下标？ {}",   networkIF.getIndex());
            log.info("速度？ {}",   networkIF.getSpeed());
        }

        final List<LogicalVolumeGroup> logicalVolumeGroups = hardware.getLogicalVolumeGroups();
        for (LogicalVolumeGroup logicalVolumeGroup : logicalVolumeGroups) {
           log.info(" logicalVolumeGroup.getName()  {}",  logicalVolumeGroup.getName());

            final Set<String> physicalVolumes = logicalVolumeGroup.getPhysicalVolumes();
            log.info(" physicalVolumes  {}",  physicalVolumes);

            final Map<String, Set<String>> logicalVolumes = logicalVolumeGroup.getLogicalVolumes();
            logicalVolumes.forEach((key, set) -> {
                log.info(" logicalVolumes key, set {}, {}",  key, set);
            });
        }
    }
}
