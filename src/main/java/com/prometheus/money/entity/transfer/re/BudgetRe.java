package com.prometheus.money.entity.transfer.re;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRe {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateTo;

    private BigDecimal amount;

    private ZonedDateTime createAt;
}
