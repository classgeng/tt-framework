package cn.cloud9.server.struct.util;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.EdidUtil;
import oshi.util.FormatUtil;
import oshi.util.ParseUtil;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月31日 下午 07:32
 */
@Slf4j
public class OperateSystemUtil {
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
    private static final HardwareAbstractionLayer hal = systemInfo.getHardware();
    private static final Properties properties = System.getProperties();
    private static final Runtime runtime = Runtime.getRuntime();

    /**
     * 读取显示器信息
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getDisplayInfo() {
        final List<Display> displays = hal.getDisplays();
        final List<Map<String, Object>> displayInfos = new ArrayList<>(displays.size());
        for (int i = 0; i < displays.size(); i++) {
            Map<String, Object> displayInfo = new ConcurrentHashMap<>();
            displayInfo.put("index", String.valueOf(i));
            final Display display = displays.get(i);

            byte[] edidBytes = display.getEdid();
            displayInfo.put("manufacturerId",  EdidUtil.getManufacturerID(edidBytes));
            displayInfo.put("productId",  EdidUtil.getProductID(edidBytes));
            displayInfo.put("isDigital",  EdidUtil.isDigital(edidBytes) ?  "Digital" : "Analog");
            displayInfo.put("serialNo",  EdidUtil.getSerialNo(edidBytes));
            displayInfo.put("manufacturerDate",  (EdidUtil.getWeek(edidBytes) * 12 / 52 + 1) + '/' + EdidUtil.getYear(edidBytes));
            displayInfo.put("version",  EdidUtil.getVersion(edidBytes));
            displayInfo.put("cmHeight", EdidUtil.getHcm(edidBytes));
            displayInfo.put("cmWidth", EdidUtil.getVcm(edidBytes));
            displayInfo.put("inHeight", EdidUtil.getHcm(edidBytes) / 2.54);
            displayInfo.put("inWidth", EdidUtil.getVcm(edidBytes) / 2.54);
            displayInfo.put("toString", String.valueOf(display));

            byte[][] descriptorBytes = EdidUtil.getDescriptors(edidBytes);
            for (byte[] bytes : descriptorBytes) {
                switch (EdidUtil.getDescriptorType(bytes)) {
                    case 0xff:
                        displayInfo.put("serialNumber", EdidUtil.getDescriptorText(bytes));
                        break;
                    case 0xfe:
                        displayInfo.put("unspecifiedText", EdidUtil.getDescriptorText(bytes));
                        break;
                    case 0xfd:
                        displayInfo.put("rangeLimits", EdidUtil.getDescriptorRangeLimits(bytes));
                        break;
                    case 0xfc:
                        displayInfo.put("monitorName", EdidUtil.getDescriptorText(bytes));
                        break;
                    case 0xfb:
                        displayInfo.put("whitePointData", ParseUtil.byteArrayToHexString(bytes));
                        break;
                    case 0xfa:
                        displayInfo.put("standardTimingId", ParseUtil.byteArrayToHexString(bytes));
                        break;
                    default:
                        if (EdidUtil.getDescriptorType(bytes) <= 0x0f && EdidUtil.getDescriptorType(bytes) >= 0x00) {
                            displayInfo.put("manufacturerData", ParseUtil.byteArrayToHexString(bytes));
                        } else {
                            displayInfo.put("preferredTiming", EdidUtil.getTimingDescriptor(bytes));
                        }
                }
            }
            displayInfos.add(displayInfo);
        }
        return displayInfos;
    }

    /**
     * 读取声卡信息
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSoundCardInfo() {
        final List<SoundCard> soundCards = hal.getSoundCards();
        final List<Map<String, Object>> soundCardInfos = new ArrayList<>(soundCards.size());
        for (int i = 0; i < soundCards.size(); i++) {
            Map<String, Object> soundCard = new ConcurrentHashMap<>();
            soundCard.put("index", String.valueOf(i));
            final SoundCard soundCardInst = soundCards.get(i);
            soundCard.put("toString", String.valueOf(soundCardInst));
            soundCard.put("codec", soundCardInst.getCodec());
            soundCard.put("name", soundCardInst.getName());
            soundCard.put("driverVersion", soundCardInst.getDriverVersion());
            soundCardInfos.add(soundCard);
        }
        return soundCardInfos;
    }

    /**
     * 读取显卡信息
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getGraphicsCardsInfo() {
        final List<GraphicsCard> graphicsCards = hal.getGraphicsCards();
        final List<Map<String, Object>> graphicsCardsInfos = new ArrayList<>(graphicsCards.size());
        for (int i = 0; i < graphicsCards.size(); i++) {
            Map<String, Object> gcInfo = new ConcurrentHashMap<>();
            gcInfo.put("index", String.valueOf(i));
            final GraphicsCard graphicsCard = graphicsCards.get(i);
            gcInfo.put("toString", String.valueOf(graphicsCard));
            gcInfo.put("name", graphicsCard.getName());
            gcInfo.put("deviceId", graphicsCard.getDeviceId());
            gcInfo.put("versionInfo", graphicsCard.getVersionInfo());
            gcInfo.put("vendor", graphicsCard.getVendor());
            gcInfo.put("vRam", graphicsCard.getVRam());
            gcInfo.put("vRamFormat", FormatUtil.formatBytes(graphicsCard.getVRam()));
            graphicsCardsInfos.add(gcInfo);
        }
        return graphicsCardsInfos;
    }

    /**
     * usb设备信息
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getUsbDevicesInfo() {
        final List<UsbDevice> usbDevices = hal.getUsbDevices(true);
        final List<Map<String, Object>> usbDevicesInfos = new ArrayList<>(usbDevices.size());
        for (int i = 0; i < usbDevices.size(); i++) {
            Map<String, Object> usbDeviceInfo = new ConcurrentHashMap<>();
            usbDeviceInfo.put("index", String.valueOf(i));
            final UsbDevice usbDevice = usbDevices.get(i);
            usbDeviceInfo.put("toString", String.valueOf(usbDevice));
            usbDeviceInfo.put("name", usbDevice.getName());
            usbDeviceInfo.put("uniqueDeviceId", usbDevice.getUniqueDeviceId());
            usbDeviceInfo.put("vendor", usbDevice.getVendor());
            usbDeviceInfo.put("productId", usbDevice.getProductId());
            usbDeviceInfo.put("serialNumber", usbDevice.getSerialNumber());
            usbDeviceInfo.put("vendorId", usbDevice.getVendorId());

            final List<UsbDevice> connectedDevices = usbDevice.getConnectedDevices();
            usbDeviceInfo.put("connectedDevices", connectedDevices);
            usbDevicesInfos.add(usbDeviceInfo);
        }
        return usbDevicesInfos;
    }

    /**
     * 网络参数信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getNetworkParamsInfo() {
        Map<String, Object> networkParamsInfo = new ConcurrentHashMap<>();
        final NetworkParams networkParams = operatingSystem.getNetworkParams();
        networkParamsInfo.put("toString", networkParams);
        networkParamsInfo.put("hostName", networkParams.getHostName());
        networkParamsInfo.put("dnsServers", Arrays.asList(networkParams.getDnsServers()));
        networkParamsInfo.put("domainName", networkParams.getDomainName());
        networkParamsInfo.put("ipv4Gateway", networkParams.getIpv4DefaultGateway());
        networkParamsInfo.put("ipv6Gateway", networkParams.getIpv6DefaultGateway());
        return networkParamsInfo;
    }

    /**
     * ip信息统计
     * @return Map<String, Object>
     */
    public static Map<String, Object> getIpStatistics() {
        final InternetProtocolStats internetProtocolStats = operatingSystem.getInternetProtocolStats();
        final Map<String, Object> ipStatisticsMap = new ConcurrentHashMap<>();

        /* tcpV4 */
        Map<String, Object> tcpV4 = new ConcurrentHashMap<>();
        final InternetProtocolStats.TcpStats tcPv4Stats = internetProtocolStats.getTCPv4Stats();
        tcpV4.put("toString", String.valueOf(tcPv4Stats));
        tcpV4.put("connectionFailures", tcPv4Stats.getConnectionFailures());
        tcpV4.put("connectionsActive", tcPv4Stats.getConnectionsActive());
        tcpV4.put("connectionsPassive", tcPv4Stats.getConnectionsPassive());
        tcpV4.put("connectionsReset", tcPv4Stats.getConnectionsReset());
        tcpV4.put("connectionsEstablished", tcPv4Stats.getConnectionsEstablished());
        tcpV4.put("inErrors", tcPv4Stats.getInErrors());
        tcpV4.put("outResets", tcPv4Stats.getOutResets());
        tcpV4.put("segmentsReceived", tcPv4Stats.getSegmentsReceived());
        tcpV4.put("segmentsRetransmitted", tcPv4Stats.getSegmentsRetransmitted());
        tcpV4.put("segmentsSent", tcPv4Stats.getSegmentsSent());
        ipStatisticsMap.put("tcpV4", tcpV4);

        /* tcpV6 */
        Map<String, Object> tcpV6 = new ConcurrentHashMap<>();
        final InternetProtocolStats.TcpStats tcPv6Stats = internetProtocolStats.getTCPv6Stats();
        tcpV6.put("toString", String.valueOf(tcPv6Stats));
        tcpV6.put("connectionFailures", tcPv6Stats.getConnectionFailures());
        tcpV6.put("connectionsActive", tcPv6Stats.getConnectionsActive());
        tcpV6.put("connectionsPassive", tcPv6Stats.getConnectionsPassive());
        tcpV6.put("connectionsReset", tcPv6Stats.getConnectionsReset());
        tcpV6.put("connectionsEstablished", tcPv6Stats.getConnectionsEstablished());
        tcpV6.put("inErrors", tcPv6Stats.getInErrors());
        tcpV6.put("outResets", tcPv6Stats.getOutResets());
        tcpV6.put("segmentsReceived", tcPv6Stats.getSegmentsReceived());
        tcpV6.put("segmentsRetransmitted", tcPv6Stats.getSegmentsRetransmitted());
        tcpV6.put("segmentsSent", tcPv6Stats.getSegmentsSent());
        ipStatisticsMap.put("tcpV6", tcpV6);

        /* udpV4 */
        Map<String, Object> udpV4 = new ConcurrentHashMap<>();
        final InternetProtocolStats.UdpStats udPv4Stats = internetProtocolStats.getUDPv4Stats();
        udpV4.put("toString", String.valueOf(udPv4Stats));
        udpV4.put("datagramsNoPort", udPv4Stats.getDatagramsNoPort());
        udpV4.put("datagramsReceived", udPv4Stats.getDatagramsReceived());
        udpV4.put("datagramsReceivedErrors", udPv4Stats.getDatagramsReceivedErrors());
        udpV4.put("datagramsSent", udPv4Stats.getDatagramsSent());
        ipStatisticsMap.put("udpV4", udpV4);

        /* udpV6 */
        Map<String, Object> udpV6 = new ConcurrentHashMap<>();
        final InternetProtocolStats.UdpStats udPv6Stats = internetProtocolStats.getUDPv6Stats();
        udpV6.put("toString", String.valueOf(udPv6Stats));
        udpV6.put("datagramsNoPort", udPv6Stats.getDatagramsNoPort());
        udpV6.put("datagramsReceived", udPv6Stats.getDatagramsReceived());
        udpV6.put("datagramsReceivedErrors", udPv6Stats.getDatagramsReceivedErrors());
        udpV6.put("datagramsSent", udPv6Stats.getDatagramsSent());
        ipStatisticsMap.put("udpV6", udpV6);

        /* connections */
//        final List<InternetProtocolStats.IPConnection> connections = internetProtocolStats.getConnections();
//        final List<Map<String, Object>> ipConnectionInfoList = new ArrayList<>(connections.size());
//        for (int i = 0; i < connections.size(); i++) {
//            Map<String, Object> ipConnectionInfo = new ConcurrentHashMap<>();
//            ipConnectionInfo.put("connectionIndex", i);
//            final InternetProtocolStats.IPConnection ipConnection = connections.get(i);
//            ipConnectionInfo.put("toString", String.valueOf(ipConnection));
//            ipConnectionInfo.put("foreignAddress", Arrays.toString(ipConnection.getForeignAddress()));
//            ipConnectionInfo.put("foreignPort", ipConnection.getForeignPort());
//            ipConnectionInfo.put("localAddress", Arrays.toString(ipConnection.getLocalAddress()));
//            ipConnectionInfo.put("state", ipConnection.getState());
//            ipConnectionInfo.put("type", ipConnection.getType());
//            ipConnectionInfo.put("localPort", ipConnection.getLocalPort());
//            ipConnectionInfo.put("owningProcessId", ipConnection.getowningProcessId());
//            ipConnectionInfo.put("receiveQueue", ipConnection.getReceiveQueue());
//            ipConnectionInfo.put("transmitQueue", ipConnection.getTransmitQueue());
//            ipConnectionInfoList.add(ipConnectionInfo);
//        }
//        ipStatisticsMap.put("ipConnections", ipConnectionInfoList);
        return ipStatisticsMap;
    }

    /**
     * 网络接口信息读取
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getNetWorkInterfaces() {
        final List<NetworkIF> networkIFs = hal.getNetworkIFs();
        final List<Map<String, Object>> networkIFList = new ArrayList<>(networkIFs.size());

        for (int i = 0; i < networkIFs.size(); i++) {
            final Map<String, Object> networkIfMap = new ConcurrentHashMap<>();
            networkIfMap.put("index", i);
            final NetworkIF networkIF = networkIFs.get(i);
            networkIfMap.put("toString", String.valueOf(networkIF));
            networkIfMap.put("displayName", networkIF.getDisplayName());
            networkIfMap.put("name", networkIF.getName());
            networkIfMap.put("ifIndex", networkIF.getIndex());
            networkIfMap.put("iPv4addr", networkIF.getIPv4addr());
            networkIfMap.put("iPv6addr", networkIF.getIPv6addr());
            networkIfMap.put("macAddr", networkIF.getMacaddr());
            networkIfMap.put("speed", networkIF.getSpeed());
            networkIfMap.put("subnetMasks", networkIF.getSubnetMasks());
            networkIfMap.put("bytesRecv", networkIF.getBytesRecv());
            networkIfMap.put("bytesSent", networkIF.getBytesSent());
            networkIfMap.put("collisions", networkIF.getCollisions());
            networkIfMap.put("ifAlias", networkIF.getIfAlias());
            networkIfMap.put("ifOperStatus", networkIF.getIfOperStatus().name());
            networkIfMap.put("ifType", networkIF.getIfType());
            networkIfMap.put("inDrops", networkIF.getInDrops());
            networkIfMap.put("inErrors", networkIF.getInErrors());
            networkIfMap.put("mtu", networkIF.getMTU());
            networkIfMap.put("ndisPhysicalMediumType", networkIF.getNdisPhysicalMediumType());
            networkIfMap.put("outErrors", networkIF.getOutErrors());
            networkIfMap.put("packetsRecv", networkIF.getPacketsRecv());
            networkIfMap.put("packetsSent", networkIF.getPacketsSent());
            networkIfMap.put("prefixLengths", networkIF.getPrefixLengths());
            networkIfMap.put("timeStamp", networkIF.getTimeStamp());
            networkIfMap.put("isConnectorPresent",  networkIF.isConnectorPresent());
            networkIfMap.put("isKnownVmMacAddr",  networkIF.isKnownVmMacAddr());
            final NetworkInterface networkInterface = networkIF.queryNetworkInterface();
            networkIfMap.put("networkInterface", String.valueOf(networkInterface));
            networkIFList.add(networkIfMap);
        }
        return networkIFList;
    }

    /**
     * 读取文件系统信息
     * @return List<Map<String, Object>>
     */
    public static Map<String, Object> getFileSystemInfo() {
        final Map<String, Object> fsInfo = new ConcurrentHashMap<>();
        final FileSystem fileSystem = operatingSystem.getFileSystem();
        final List<Map<String, Object>> fileSystemInfos = new ArrayList<>();

        fsInfo.put("openFileDescriptors", fileSystem.getOpenFileDescriptors());
        fsInfo.put("maxFileDescriptors", fileSystem.getMaxFileDescriptors());
        fsInfo.put("fileDescriptors", String.format("%d/%d", fileSystem.getOpenFileDescriptors(), fileSystem.getMaxFileDescriptors()));
        fsInfo.put("fdUsageRate", String.format("%.2f", (100d * fileSystem.getOpenFileDescriptors() / fileSystem.getMaxFileDescriptors())) + "%");

        final List<OSFileStore> fileStores = fileSystem.getFileStores();
        for (int i = 0; i < fileStores.size(); i++) {
            final Map<String, Object> fileStoreInfo = new ConcurrentHashMap<>();
            fileStoreInfo.put("index", i);
            final OSFileStore osFileStore = fileStores.get(i);
            /* fileStoreInfo.put("instance", osFileStore); */
            fileStoreInfo.put("name", osFileStore.getName());
            fileStoreInfo.put("freeSpace", osFileStore.getFreeSpace());
            fileStoreInfo.put("label", osFileStore.getLabel());
            fileStoreInfo.put("type", osFileStore.getType());
            fileStoreInfo.put("uuid", osFileStore.getUUID());
            fileStoreInfo.put("options", osFileStore.getOptions());
            fileStoreInfo.put("description", osFileStore.getDescription());

            fileStoreInfo.put("volume", osFileStore.getVolume());
            fileStoreInfo.put("mount", osFileStore.getMount());
            fileStoreInfo.put("logicalVolume", osFileStore.getLogicalVolume());
            fileStoreInfo.put("totalInodes", FormatUtil.formatValue(osFileStore.getTotalInodes(), ""));
            fileStoreInfo.put("freeInodes", FormatUtil.formatValue(osFileStore.getFreeInodes(), ""));
            fileStoreInfo.put("inodesUsageRate", String.format("%.2f", 100d * osFileStore.getFreeInodes() / osFileStore.getTotalInodes()) + "%");

            /* space参数 */
            fileStoreInfo.put("srcTotalSpace", osFileStore.getTotalSpace());
            fileStoreInfo.put("srcUsedSpace", osFileStore.getTotalSpace() - osFileStore.getUsableSpace());
            fileStoreInfo.put("srcUsableSpace", osFileStore.getUsableSpace());
            fileStoreInfo.put("totalSpace", FormatUtil.formatBytes(osFileStore.getTotalSpace()));
            fileStoreInfo.put("usedSpace",  FormatUtil.formatBytes(osFileStore.getTotalSpace() - osFileStore.getUsableSpace()));
            fileStoreInfo.put("usableSpace",  FormatUtil.formatBytes(osFileStore.getUsableSpace()));
            fileStoreInfo.put("usageRate", String.format("%.2f", 100d * (osFileStore.getTotalSpace() - osFileStore.getUsableSpace()) / osFileStore.getTotalSpace()) + "%");

            fileSystemInfos.add(fileStoreInfo);
        }
        fsInfo.put("fileStores", fileSystemInfos);

        return fsInfo;
    }

    /**
     * 逻辑卷组信息
     * @return  List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getLogicalVolumeGroupInfo() {
        final List<LogicalVolumeGroup> logicalVolumeGroups = hal.getLogicalVolumeGroups();
        final List<Map<String, Object>> lvgInfos = new ArrayList<>(logicalVolumeGroups.size());
        for (int i = 0; i < logicalVolumeGroups.size(); i++) {
            final LogicalVolumeGroup lvg = logicalVolumeGroups.get(i);
            final Map<String, Object> lvgInfo = new ConcurrentHashMap<>();
            lvgInfo.put("index", i);
            lvgInfo.put("toString", String.valueOf(lvg));
            lvgInfo.put("name", lvg.getName());
            lvgInfo.put("logicalVolumes", lvg.getLogicalVolumes());
            lvgInfo.put("physicalVolumes", lvg.getPhysicalVolumes());
            lvgInfos.add(lvgInfo);
        }
        return lvgInfos;
    }

    /**
     * 磁盘存储信息读取
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getDiskStoreInfo() {
        final List<HWDiskStore> diskStores = hal.getDiskStores();
        final List<Map<String, Object>> dsList = new ArrayList<>(diskStores.size());
        for (int i = 0; i < diskStores.size(); i++) {
            final HWDiskStore hwDiskStore = diskStores.get(i);
            final Map<String, Object> hwDsMap = new ConcurrentHashMap<>();
            hwDsMap.put("index", i);
            hwDsMap.put("toString", String.valueOf(hwDiskStore));
            hwDsMap.put("name", hwDiskStore.getName());
            hwDsMap.put("currentQueueLength", hwDiskStore.getCurrentQueueLength());
            hwDsMap.put("model", hwDiskStore.getModel());
            hwDsMap.put("serial", hwDiskStore.getSerial());
            hwDsMap.put("size", FormatUtil.formatBytes(hwDiskStore.getSize()));
            hwDsMap.put("reads", FormatUtil.formatBytes(hwDiskStore.getReads()));
            hwDsMap.put("writes", FormatUtil.formatBytes(hwDiskStore.getWrites()));
            hwDsMap.put("readBytes", hwDiskStore.getReadBytes());
            hwDsMap.put("writeBytes", hwDiskStore.getWriteBytes());
            hwDsMap.put("transferTime", hwDiskStore.getTransferTime());
            hwDsMap.put("timeStamp", hwDiskStore.getTimeStamp());

            final List<HWPartition> partitions = hwDiskStore.getPartitions();
            final List<Map<String, Object>> partitionList = new ArrayList<>(partitions.size());
            for (HWPartition partition : partitions) {
                final Map<String, Object> partitionMap = new ConcurrentHashMap<>();
                partitionMap.put("toString", partition);
                partitionMap.put("size", FormatUtil.formatBytes(partition.getSize()));
                partitionMap.put("name", partition.getName());
                partitionMap.put("type", partition.getType());
                partitionMap.put("identification", partition.getIdentification());
                partitionMap.put("major", partition.getMajor());
                partitionMap.put("uuid", partition.getUuid());
                partitionMap.put("mountPoint", partition.getMountPoint());
                partitionMap.put("minor", partition.getMinor());
                partitionList.add(partitionMap);
            }
            hwDsMap.put("partitionList", partitionList);
            dsList.add(hwDsMap);
        }
        return dsList;
    }

    /**
     * 电源信息读取
     * @return List<Map<String, Object>
     */
    public static List<Map<String, Object>> getPowerSourceInfo() {
        final List<PowerSource> powerSources = hal.getPowerSources();
        final List<Map<String, Object>> powerSourcesList = new ArrayList<>(powerSources.size());
        for (PowerSource powerSource : powerSources) {
            final Map<String, Object> powerSourceMap = new ConcurrentHashMap<>();
            powerSourceMap.put("toString", String.valueOf(powerSource));
            powerSourceMap.put("amperage", powerSource.getAmperage());
            powerSourceMap.put("name", powerSource.getName());
            powerSourceMap.put("capacityUnits", powerSource.getCapacityUnits());
            powerSourceMap.put("serialNumber", powerSource.getSerialNumber());
            powerSourceMap.put("currentCapacity", powerSource.getCurrentCapacity());
            powerSourceMap.put("deviceName", powerSource.getDeviceName());
            powerSourceMap.put("manufacturer", powerSource.getManufacturer());
            powerSourceMap.put("voltage", powerSource.getVoltage());
            powerSourceMap.put("chemistry", powerSource.getChemistry());
            powerSourceMap.put("cycleCount", powerSource.getCycleCount());
            powerSourceMap.put("powerUsageRate", powerSource.getPowerUsageRate());
            powerSourceMap.put("designCapacity", powerSource.getDesignCapacity());
            powerSourceMap.put("maxCapacity", powerSource.getMaxCapacity());
            // powerSourceMap.put("manufactureDate", powerSource.getManufactureDate());
            powerSourceMap.put("temperature", powerSource.getTemperature());
            powerSourceMap.put("isDischarging", powerSource.isDischarging());
            powerSourceMap.put("isCharging", powerSource.isCharging());
            powerSourceMap.put("isPowerOnLine", powerSource.isPowerOnLine());
            powerSourceMap.put("timeRemainingInstant", powerSource.getTimeRemainingInstant());
            powerSourceMap.put("timeRemainingEstimated", powerSource.getTimeRemainingEstimated());
            powerSourceMap.put("remainingCapacityPercent", powerSource.getRemainingCapacityPercent());
            powerSourcesList.add(powerSourceMap);
        }
        return powerSourcesList;
    }


    /**
     * 获取系统服务信息，服务基于系统平台决定
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSystemServiceInfo() {
        final List<OSService> services = operatingSystem.getServices();
        final List<Map<String, Object>> systemServiceList = new ArrayList<>(services.size());

        for (int i = 0; i < services.size(); i++) {
            final OSService osService = services.get(i);
            final Map<String, Object> osServiceMap = new ConcurrentHashMap<>();
            osServiceMap.put("index", i);
            osServiceMap.put("toString", String.valueOf(osService));
            osServiceMap.put("instance", osService);
            osServiceMap.put("state", osService.getState().name());
            osServiceMap.put("pid", osService.getProcessID());
            osServiceMap.put("name", osService.getName());
            systemServiceList.add(osServiceMap);
        }
        return systemServiceList;
    }

    /**
     * 获取传感器信息
     * @return String
     */
    public static Map<String, Object> getSensorInfo() {
        final Map<String, Object> sensorInfo = new ConcurrentHashMap<>();
        final Sensors sensors = hal.getSensors();
        sensorInfo.put("toString", String.valueOf(sensors));
        sensorInfo.put("instance", sensors);
        return sensorInfo;
    }

    /**
     * 获取进程信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getProcessesInfo() {
        final GlobalMemory globalMemory = hal.getMemory();
        final Map<String, Object> processesInfoMap = new ConcurrentHashMap<>();
        processesInfoMap.put("processCount", operatingSystem.getProcessCount());
        processesInfoMap.put("threadCount", operatingSystem.getThreadCount());

        List<OSProcess> osProcessList = operatingSystem.getProcesses(OperatingSystem.ProcessFiltering.ALL_PROCESSES, OperatingSystem.ProcessSorting.CPU_DESC, 100);
        final List<Map<String, Object>> osProcessMapList = new ArrayList<>(osProcessList.size());
        for (int i = 0; i < osProcessList.size(); i++) {
            final OSProcess osProcess = osProcessList.get(i);
            final Map<String, Object> osProcessMap = new ConcurrentHashMap<>();
            osProcessMap.put("index", i);
            osProcessMap.put("toString", String.valueOf(osProcess));
            osProcessMap.put("pid", osProcess.getProcessID());
            osProcessMap.put("kernelTime", osProcess.getKernelTime());
            osProcessMap.put("userTime", osProcess.getUserTime());
            osProcessMap.put("upTime", osProcess.getUpTime());
            osProcessMap.put("startTime", osProcess.getStartTime());
            osProcessMap.put("bytesRead", osProcess.getBytesRead());
            osProcessMap.put("bytesWritten", osProcess.getBytesWritten());
            osProcessMap.put("openFiles", osProcess.getOpenFiles());
            osProcessMap.put("softOpenFileLimit", osProcess.getSoftOpenFileLimit());
            osProcessMap.put("hardOpenFileLimit", osProcess.getHardOpenFileLimit());
            osProcessMap.put("processCpuLoadCumulative", osProcess.getProcessCpuLoadCumulative());
            osProcessMap.put("processCpuLoadBetweenTicks", osProcess.getProcessCpuLoadBetweenTicks(osProcess));
            osProcessMap.put("bitness", osProcess.getBitness());
            osProcessMap.put("affinityMask", osProcess.getAffinityMask());
            osProcessMap.put("minorFaults", osProcess.getMinorFaults());
            osProcessMap.put("majorFaults", osProcess.getMajorFaults());
            osProcessMap.put("priority", osProcess.getPriority());
            osProcessMap.put("threadCount", osProcess.getThreadCount());
            osProcessMap.put("group", osProcess.getGroup());
            osProcessMap.put("groupId", osProcess.getGroupID());
            osProcessMap.put("user", osProcess.getUser());
            osProcessMap.put("userId", osProcess.getUserID());
            osProcessMap.put("currentWorkingDirectory", osProcess.getCurrentWorkingDirectory());
            osProcessMap.put("path", osProcess.getPath());
            osProcessMap.put("arguments", osProcess.getArguments());
            osProcessMap.put("environmentVariables", osProcess.getEnvironmentVariables());
            osProcessMap.put("cpuUsageRate", String.format("%.2f", 100d * (osProcess.getKernelTime() + osProcess.getUserTime()) / osProcess.getUpTime()) + "%");
            osProcessMap.put("memUsageRate", String.format("%.2f", 100d * osProcess.getResidentSetSize() / globalMemory.getTotal()) + "%");
            osProcessMap.put("virtualMemSize", FormatUtil.formatBytes(osProcess.getVirtualSize()));
            osProcessMap.put("residentSetSize", FormatUtil.formatBytes(osProcess.getResidentSetSize()));
            osProcessMap.put("processName", osProcess.getName());
            osProcessMapList.add(osProcessMap);
        }
        processesInfoMap.put("osProcessMapList", osProcessMapList);
        return processesInfoMap;
    }

    /**
     * 获取内存信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getMemoryInfo() {
        final GlobalMemory globalMemory = hal.getMemory();
        final Map<String, Object> gmMap = new ConcurrentHashMap<>();

        long total = globalMemory.getTotal();
        long available = globalMemory.getAvailable();
        long used = globalMemory.getTotal() - available;
        gmMap.put("srcTotal", total);
        gmMap.put("srcAvailable", available);
        gmMap.put("srcUsed", used);
        gmMap.put("total", FormatUtil.formatBytes(total));
        gmMap.put("available", FormatUtil.formatBytes(available));
        gmMap.put("used", FormatUtil.formatBytes(used));
        gmMap.put("usageRate", String.format("%.2f", 100d * used / globalMemory.getTotal()) + "%");
        gmMap.put("freeRate", String.format("%.2f", 100d * available / globalMemory.getTotal()) + "%");
        gmMap.put("pageSize", globalMemory.getPageSize());

        final VirtualMemory virtualMemory = globalMemory.getVirtualMemory();
        final Map<String, Object> vmMap = new ConcurrentHashMap<>();
        vmMap.put("instance", virtualMemory);
        vmMap.put("toString", virtualMemory.toString());
        long swapTotal = virtualMemory.getSwapTotal();
        long swapUsed = virtualMemory.getSwapUsed();
        long swapFree = swapTotal - swapUsed;

        vmMap.put("srcSwapTotal",  swapTotal);
        vmMap.put("srcSwapUsed",  swapUsed);
        vmMap.put("srcSwapFree",  swapFree);
        vmMap.put("swapTotal",  FormatUtil.formatBytes(swapTotal));
        vmMap.put("swapUsed",  FormatUtil.formatBytes(swapUsed));
        vmMap.put("swapFree",  FormatUtil.formatBytes(swapFree));
        vmMap.put("swapUsageRate", String.format("%.2f", 100d * swapUsed / swapTotal) + "%");
        vmMap.put("swapFreeRate", String.format("%.2f", 100d * swapFree / swapTotal) + "%");

        long virtualMax = virtualMemory.getVirtualMax();
        long virtualInUse = virtualMemory.getVirtualInUse();
        long virtualFree = virtualMax - virtualInUse;
        vmMap.put("srcVirtualMax",  virtualMax);
        vmMap.put("srcVirtualInUse",  virtualInUse);
        vmMap.put("srcVirtualFree",  virtualFree);
        vmMap.put("virtualMax", FormatUtil.formatBytes(virtualMax));
        vmMap.put("virtualInUse", FormatUtil.formatBytes(virtualInUse));
        vmMap.put("virtualFree", FormatUtil.formatBytes(virtualFree));
        vmMap.put("virtualUsageRate", String.format("%.2f", 100d * virtualInUse / virtualMax) + "%");
        vmMap.put("virtualFreeRate", String.format("%.2f", 100d * virtualFree / virtualMax) + "%");

        gmMap.put("virtualMemory", vmMap);

        final List<PhysicalMemory> physicalMemoryList = globalMemory.getPhysicalMemory();
        final List<Map<String, Object>> pmInfoList = new ArrayList<>(physicalMemoryList.size());
        for (PhysicalMemory pm : physicalMemoryList) {
            final Map<String, Object> pmMap = new ConcurrentHashMap<>();
            pmMap.put("toString", String.valueOf(pm));
            pmMap.put("instance", pm);
            pmMap.put("bankLabel", pm.getBankLabel());
            pmMap.put("manufacturer", pm.getManufacturer());
            pmMap.put("capacity",  FormatUtil.formatBytes(pm.getCapacity()));
            pmMap.put("memoryType", pm.getMemoryType());
            pmMap.put("clockSpeed", FormatUtil.formatHertz(pm.getClockSpeed()));
            pmInfoList.add(pmMap);
        }
        gmMap.put("physicalMemoryList", pmInfoList);
        return gmMap;
    }

    /**
     * 获取操作系统信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getOperateSystemInfo() {
        final Map<String, Object> osInfo = new ConcurrentHashMap<>();
        osInfo.put("osName", String.valueOf(operatingSystem));
        osInfo.put("booted", Instant.ofEpochSecond(operatingSystem.getSystemBootTime()));
        osInfo.put("sessionList", operatingSystem.getSessions());

        final ComputerSystem computerSystem = hal.getComputerSystem();
        final Map<String, Object> csInfo = new ConcurrentHashMap<>();
        csInfo.put("serialNumber", computerSystem.getSerialNumber());
        csInfo.put("manufacturer", computerSystem.getManufacturer());
        csInfo.put("hardwareUUID", computerSystem.getHardwareUUID());
        csInfo.put("model", computerSystem.getModel());
        osInfo.put("computerSystem", csInfo);

        /* 固件信息 */
        Firmware firmware = computerSystem.getFirmware();
        final Map<String, Object> firmwareInfo = new ConcurrentHashMap<>();
        firmwareInfo.put("manufacturer", firmware.getManufacturer());
        firmwareInfo.put("name", firmware.getName());
        firmwareInfo.put("description", firmware.getDescription());
        firmwareInfo.put("releaseDate", firmware.getReleaseDate());
        firmwareInfo.put("version", firmware.getVersion());
        osInfo.put("firmware", firmwareInfo);

        /* 主板信息 */
        Baseboard baseboard = computerSystem.getBaseboard();
        final Map<String, Object> baseboardInfo = new ConcurrentHashMap<>();
        baseboardInfo.put("manufacturer", baseboard.getManufacturer());
        baseboardInfo.put("model", baseboard.getModel());
        baseboardInfo.put("serialNumber", baseboard.getSerialNumber());
        baseboardInfo.put("version", baseboard.getVersion());
        osInfo.put("baseboard", baseboardInfo);

        return osInfo;
    }

    /**
     * 获取操作系统信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getCpuInfo() {
        final Map<String, Object> cpuInfo = new ConcurrentHashMap<>();
        CpuInfo cpuInfo1 = OshiUtil.getCpuInfo();
        final CentralProcessor processor = hal.getProcessor();
        cpuInfo.put("toString", String.valueOf(processor));
        cpuInfo.put("instance", processor);
        cpuInfo.put("utilInstance", cpuInfo1);
        return cpuInfo;
    }

    /**
     * https://www.cnblogs.com/koal/p/4391127.html
     * https://blog.csdn.net/Stephanie17395/article/details/117327578
     * 获取操作JVM信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getJvmInfo() {
        final Map<String, Object> jvmInfo = new ConcurrentHashMap<>();
        jvmInfo.put("version", properties.getProperty("java.version"));
        jvmInfo.put("classVersion", properties.getProperty("java.class.version"));
        jvmInfo.put("jvmVersion", properties.getProperty("java.vm.version"));
        jvmInfo.put("jvmVendor", properties.getProperty("java.vm.vendor"));
        jvmInfo.put("jvmVendorUrl", properties.getProperty("java.vendor.url"));
        jvmInfo.put("jvmSpecificationVendor", properties.getProperty("java.specification.vendor"));
        jvmInfo.put("jvmSpecificationVersion", properties.getProperty("java.vm.specification.version"));
        jvmInfo.put("jvmSpecificationName", properties.getProperty("java.vm.specification.name"));
        jvmInfo.put("javaSpecificationVersion", properties.getProperty("java.specification.version"));
        jvmInfo.put("javaSpecificationName", properties.getProperty("java.specification.name"));
        jvmInfo.put("execCommand", properties.getProperty("sun.java.command"));
        jvmInfo.put("compiler", properties.getProperty("sun.management.compiler"));
        jvmInfo.put("jvmName", properties.getProperty("java.vm.name"));
        jvmInfo.put("jvmMode", properties.getProperty("java.vm.info"));
        jvmInfo.put("archDataModel", properties.getProperty("sun.arch.data.model"));
        jvmInfo.put("runtimeVersion", properties.getProperty("java.runtime.version"));
        jvmInfo.put("runtimeName", properties.getProperty("java.runtime.name"));
        jvmInfo.put("home", properties.getProperty("java.home"));
        jvmInfo.put("libPath", properties.getProperty("java.library.path").split(";"));
        jvmInfo.put("classPath", properties.getProperty("java.class.path").split(";"));
        jvmInfo.put("jreBinPath", properties.getProperty("sun.boot.library.path"));
        jvmInfo.put("bootClassPath", properties.getProperty("sun.boot.class.path").split(";"));
        jvmInfo.put("launcher", properties.getProperty("sun.java.launcher"));

        jvmInfo.put("extendsDir", properties.getProperty("java.ext.dirs").split(";"));
        /* io临时目录 */
        jvmInfo.put("ioTmpDir", properties.getProperty("java.io.tmpdir"));

        return jvmInfo;
    }

    public static Map<String, Object> getServerSysInfo() {
        final Map<String, Object> serverSysInfo = new ConcurrentHashMap<>();

        /* 操作系统名称 */
        serverSysInfo.put("osName", properties.getProperty("os.name"));

        /* 操作系统版本 */
        serverSysInfo.put("osVersion", properties.getProperty("os.version"));

        /* 操作系统架构 */
        serverSysInfo.put("osArch", properties.getProperty("os.arch"));

        /* 应用所在目录 */
        serverSysInfo.put("userDir", properties.getProperty("user.dir"));

        /* 当前用户主目录 */
        serverSysInfo.put("userHome", properties.getProperty("user.home"));

        /* 用户名 */
        serverSysInfo.put("userName", properties.getProperty("user.name"));

        /* 用户变量 */
        serverSysInfo.put("userVariant", properties.getProperty("user.variant").split(";"));

        /* 用户时区 */
        serverSysInfo.put("userTimezone", properties.getProperty("user.timezone"));

        /* 用户语言 */
        serverSysInfo.put("userLanguage", properties.getProperty("user.language"));

        /* 系统环境变量 */
        Map<String, String> getenv = System.getenv();
        final Map<String, Object> sysEnvInfo = new ConcurrentHashMap<>(getenv);
        sysEnvInfo.put("PSModulePath", String.valueOf(sysEnvInfo.get("PSModulePath")).split(";"));
        sysEnvInfo.put("Path", String.valueOf(sysEnvInfo.get("Path")).split(";"));
        serverSysInfo.put("sysEnv", sysEnvInfo);

        /* 获取应用启动时间和运行时长 */
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        LocalDateTime start = Instant.ofEpochMilli(startTime).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        serverSysInfo.put("startTime", start);

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(start, now);
        final Map<String, Object> durationInfo = new ConcurrentHashMap<>();
        durationInfo.put("day", duration.toDays());
        durationInfo.put("hour", duration.toHours());
        durationInfo.put("min", duration.toMinutes());
        durationInfo.put("sec", duration.toMillis() / 1000L);
        serverSysInfo.put("duration", durationInfo);

        serverSysInfo.put("bootParam", ManagementFactory.getRuntimeMXBean().getInputArguments());
        return serverSysInfo;
    }

    /**
     * 获取JVM内存信息
     * @return Map<String, Object>
     */
    public static Map<String, Object> getJvmMemory() {
        final Map<String, Object> jvmMemory = new ConcurrentHashMap<>();
        long totalMem = runtime.totalMemory();
        long freeMem = runtime.freeMemory();
        long usedMem = totalMem - freeMem;
        long maxMem = runtime.maxMemory();
        jvmMemory.put("srcTotal", totalMem);
        jvmMemory.put("srcFree", freeMem);
        jvmMemory.put("srcUsed", usedMem);
        jvmMemory.put("total", FormatUtil.formatBytes(totalMem));
        jvmMemory.put("max", FormatUtil.formatBytes(maxMem));
        jvmMemory.put("free", FormatUtil.formatBytes(freeMem));
        jvmMemory.put("used", FormatUtil.formatBytes(usedMem));
        jvmMemory.put("usageRate", String.format("%.2f", 100D * usedMem / totalMem) + "%");
        jvmMemory.put("freeRate", String.format("%.2f", 100D * freeMem / totalMem) + "%");
        return jvmMemory;
    }

    public static Map<String, Object> getAppThreadInfo() {
        final Map<String, Object> threadInfo = new ConcurrentHashMap<>();
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();

        int activeCount = currentGroup.activeCount();
        Thread[] activeThreads = new Thread[activeCount];
        currentGroup.enumerate(activeThreads);
        threadInfo.put("activeThread", Arrays.asList(activeThreads));

        return threadInfo;
    }
}
