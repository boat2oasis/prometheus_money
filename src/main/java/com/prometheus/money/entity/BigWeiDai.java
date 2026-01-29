package com.prometheus.money.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * @since 2025-08-30
 */
@Getter
@Setter
@TableName("big_wei_dai")
public class BigWeiDai implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Double weight;

    private Integer carbohydrate;
    
    private Integer calorieDiff;
    
    private Integer calorieDifff;
    
    private Integer protein;
    
    private Integer fat;
    
    private Integer exercise;
    
    private Integer bmr;
    
    private Integer bmrr;
    
    private Integer totalInput;
    
    private Integer effectiveInput;
    
    private Float offset;

    private LocalDateTime todayIs;

    private LocalDateTime createTime;
}
