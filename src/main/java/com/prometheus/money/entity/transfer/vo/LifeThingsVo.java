package com.prometheus.money.entity.transfer.vo;

import java.math.BigDecimal;
import java.util.List;

import com.prometheus.money.entity.LifeThings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeThingsVo extends LifeThings {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LifeThingsVo> children;

	private Integer type;

	private Integer expend = 0;
}
