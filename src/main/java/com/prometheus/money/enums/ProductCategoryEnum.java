package com.prometheus.money.enums;

public enum ProductCategoryEnum {
    商品(1, "商品"),       // Product
    服务(2, "服务"),       // Service
    商品服务(3, "商品服务"); // Both Product and Service

    private final Integer code;
    private final String value;

    ProductCategoryEnum(Integer code, String value) {
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
        for (ProductCategoryEnum category : ProductCategoryEnum.values()) {
            if (category.getCode().equals(code)) {
                return category.getValue();
            }
        }
        return null; // 找不到匹配的code时返回null
    }
}
