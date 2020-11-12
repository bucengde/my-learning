package com.learning.vps.eunm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerFlagEnum {
    // SMS
    SMS_AW("AW", "昂网"),
    SMS_9527("9527", "9527"),


    ;

    private String code;
    private String name;
}
