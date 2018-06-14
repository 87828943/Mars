package com.mars.exception;

public enum MarsException {

    FAILED("99999","失败"),
    PARAM_EXCEPTION("00001","参数错误");

    private String code;
    private String msg;

    MarsException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
