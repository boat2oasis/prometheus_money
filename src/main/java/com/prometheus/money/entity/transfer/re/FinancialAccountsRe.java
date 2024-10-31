package com.prometheus.money.entity.transfer.re;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialAccountsRe {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private String accountName;

	private String accountIcon;

	private BigDecimal accountBalance;

	private Date createAt;

	private Integer defaultAccount;
}

