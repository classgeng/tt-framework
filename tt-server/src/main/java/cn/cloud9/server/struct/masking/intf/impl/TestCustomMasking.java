package cn.cloud9.server.struct.masking.intf.impl;

import cn.cloud9.server.struct.masking.intf.CustomMasking;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月27日 下午 01:07
 */
public class TestCustomMasking extends CustomMasking {

    @Override
    public String masking(String data) {
        return "具体脱敏实现。。。";
    }
}
