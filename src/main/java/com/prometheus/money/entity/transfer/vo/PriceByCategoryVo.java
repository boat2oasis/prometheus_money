package com.prometheus.money.entity.transfer.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceByCategoryVo {
	private BigDecimal pricees;
	private String categoryName;
	private Integer category;
}
