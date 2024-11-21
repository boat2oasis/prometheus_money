package com.prometheus.money.entity.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SS {
	@ExcelProperty("A股代码")
    private String aStockCode;
	
	@ExcelProperty("B股代码")
    private String bStockCode;

    @ExcelProperty("证券简称")
    private String aSimpleName;

    @ExcelProperty("扩位证券简称")
    private String aExpendSimpleName;
    
    @ExcelProperty("公司英文全称")
    private String aEnglishName;
    
    @ExcelProperty("上市日期")
    private String InitialPublicOfferingTime;
}
