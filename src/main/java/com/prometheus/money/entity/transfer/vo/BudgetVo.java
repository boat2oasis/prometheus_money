package com.prometheus.money.entity.transfer.vo;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetVo {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private ZonedDateTime dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private ZonedDateTime dateTo;

    private BigDecimal amount;
    
    /**
     * 已使用额度
     */
    private BigDecimal usedAmount;
    
    private BigDecimal couldSaveAmount;
    
    /**
     * 已使用日均
     */
    private BigDecimal usedAmountAvg;
    
    /**
     * 可用额度
     */
    private BigDecimal  availableAmount;
    
    /**
     * 可使用日均
     */
    private BigDecimal availableAmountAvg;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private ZonedDateTime createAt;

}
