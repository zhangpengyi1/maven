package com.zxs.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface CommonConsts {

    @Getter
    @AllArgsConstructor
    enum OnOff {
        OFF("0", "禁用"),
        ON("1", "启用");

        String code;
        String desc;
    }

    @Getter
    @AllArgsConstructor
    enum status {
        CODE_0("0", "失败"),
        CODE_1("1", "成功");

        String code;
        String desc;
    }

}
