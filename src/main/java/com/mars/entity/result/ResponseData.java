package com.mars.entity.result;

import com.mars.exception.MarsException;

public class ResponseData {

    //返回编码
    private String resCode = "00000";

    //返回信息
    private String resMsg = "成功";

    //返回数据
    private Object data;

    public ResponseData() {
    }

    public ResponseData(MarsException e) {
        this.resCode = e.getCode();
        this.resMsg = e.getMsg();
    }

    public ResponseData(Object obj) {
        this.data = obj;
    }


    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
