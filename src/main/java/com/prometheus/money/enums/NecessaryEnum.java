package com.prometheus.money.enums;

public enum NecessaryEnum {

    NECESSARY(1, "必要"),       // Product
    LITTLE_NECESSARY(2, "有点必要"),       // Service
    NOT_NECESSARY(3, "没必要"); // Both Product and Service

    private final Integer code;
    private final String value;

    NecessaryEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    // 通过code获取对应的value
    public static String getValueByCode(Integer code) {
        for (NecessaryEnum necessary : NecessaryEnum.values()) {
            if (necessary.getCode().equals(code)) {
                return necessary.getValue();
            }
        }
        return null; // 找不到匹配的code时返回null
    }

}
