package com.mmall.common;

/**
 * Created by Administrator on 2017/7/10.
 */
public enum ResponeCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;

    private final String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ResponeCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
