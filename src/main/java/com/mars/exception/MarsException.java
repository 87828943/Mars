package com.mars.exception;

public enum MarsException {

    FAILED("99999","失败"),
    PARAM_EXCEPTION("00001","参数错误"),

    //user相关:100001
    EMAIL_EXIST("10001","邮箱已注册！"),
    NAME_EXIST("10002","昵称已占用"),

    //test:200001
    TEST("20001","测试占用");

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
