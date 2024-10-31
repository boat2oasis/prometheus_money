package com.prometheus.money.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-22
 */
@Getter
@Setter
@TableName("financial_accounts")
public class FinancialAccounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String accountName;

    private String accountIcon;

    private BigDecimal accountBalance;

    private Date createAt;
    
    private Integer defaultAccount;
}
