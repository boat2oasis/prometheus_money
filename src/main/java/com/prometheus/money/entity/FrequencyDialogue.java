package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2025-01-11
 */
@Getter
@Setter
@TableName("frequency_dialogue")
public class FrequencyDialogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer frequencyId;

    private String series;

    private Integer season;

    private Integer episode;

    private String chinese;

    private String sentence;

    private Boolean review;
}
