package cn.cloud9.server.system.common.area.service;

import cn.cloud9.server.system.common.area.dto.AreaDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface IAreaService {

    List<AreaDTO> getAreaList(AreaDTO area);

    List<AreaDTO> getAreaTree();

    IPage<AreaDTO> getAreaPage(AreaDTO dto);

}
