package com.prometheus.money.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

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
 * @since 2024-11-04
*/
@Setter
@Getter
public class Sentences implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private String words;

    private String source;
    
    private Integer hard;

    private LocalDateTime createdAt;

    private String tip;
    
    private Integer problemable;
}
