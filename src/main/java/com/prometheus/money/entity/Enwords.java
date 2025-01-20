package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-26
 */
@Getter
@Setter
public class Enwords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("word")
    private String word;

    private String translation;
}
