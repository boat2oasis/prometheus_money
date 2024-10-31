package com.prometheus.money.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
@TableName("spent_information")
public class SpentInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String productServiceName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal pricees;

    private ZonedDateTime spentDate;

    private LocalDateTime createAt;

    private Integer category;

    private Integer usedFor;
    
    private Integer accountId;
    
    private Integer necessary;
    
    private BigDecimal couldSave;
}
