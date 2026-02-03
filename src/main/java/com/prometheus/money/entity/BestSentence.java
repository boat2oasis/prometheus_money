package com.prometheus.money.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class BestSentence implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * English Sentence
     */
    private String english;

    /**
     * Create Time
     */
    private Date createTime;
}
