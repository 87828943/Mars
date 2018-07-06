package com.mars.entity.enums;

public enum BillTypeEnum {

    OTHERS(0,"其他"),
    PLAYGAMES(1,"游戏"),
    ONLINESHOPPING(2,"网购"),
    EAT(3,"饮食");

    private Integer code;
    private String desc;

    BillTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code){
        for (BillTypeEnum billType : BillTypeEnum.values()){
            if (billType.code.equals(code)){
                return billType.getDesc();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
