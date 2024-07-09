package cn.cloud9.server.reptile.kfc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KfcStoreDTO {
    // 门店名称
    private String storeName;
    // 省份名
    private String provinceName;
    // 城市名称
    private String cityName;
    // 这个门店可支持的功能描述
    private String pro;
    // 详细地址
    private String addressDetail;
    // 创建时间
    private LocalDateTime genTime;
}
