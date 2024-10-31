package com.prometheus.money.enums;

public enum ProductPurposeEnum {
    吃(1, "吃"),               // Eating
    喝(2, "喝"),               // Drinking
    住(3, "用"),               // Housing               // Utility
    行(4, "行"),               // Transportation
    话费网费(5, "话费网费"),    // Phone and Internet Bills
    水费(6, "水费"),           // Rent
    电费(7, "电费"),           // Water Bill
    房租住宿(8, "房租住宿"),
	休闲娱乐(9, "休闲娱乐");  // Electricity Bill

    private final Integer code;
    private final String value;

    ProductPurposeEnum(Integer code, String value) {
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
        for (ProductPurposeEnum purpose : ProductPurposeEnum.values()) {
            if (purpose.getCode().equals(code)) {
                return purpose.getValue();
            }
        }
        return null; // 找不到匹配的code时返回null
    }
}
