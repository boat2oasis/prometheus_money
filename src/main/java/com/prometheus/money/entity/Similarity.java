package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2025-01-24
 */
@Getter
@Setter
public class Similarity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer frequencyId;
    private String  frequencyWord;

    private String similarityWord;

    private Integer frequency;

    private Integer coca;

    private BigDecimal similarity;

    private BigDecimal levenshtein;
}
