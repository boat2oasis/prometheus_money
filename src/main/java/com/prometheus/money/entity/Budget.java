package com.prometheus.money.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-24
 */
@Getter
@Setter
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateTo;

    private BigDecimal amount;
    

    private ZonedDateTime createAt;
}
