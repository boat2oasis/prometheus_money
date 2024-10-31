package com.prometheus.money.entity.transfer.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PriceByUsedForVo {
	private Integer usedFor;
	private BigDecimal pricees;
	private String usedForName;
}