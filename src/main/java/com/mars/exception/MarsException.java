package com.mars.exception;

public enum MarsException {

    FAILED("99999","失败"),
    PARAM_EXCEPTION("00001","参数错误"),

    //user相关:100001
    EMAIL_EXIST("10001","邮箱已注册！"),
    NAME_EXIST("10002","昵称已占用！"),
    USER_EXIST("10003","用户不存在！"),
    PASSWORD_ERROR("10004","密码不正确！"),
    SENDEMAIL_ERROR("10005","发送邮件失败！请稍后再试！"),
    LINK_VALIDE("10006","该链接已过期，请重新请求"),

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
