package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 意见反馈表
 * </p>
 *
 * @author Heisenberg
 * @since 2026-02-02
 */
@Getter
@Setter
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户IP
     */
    private String ip;
}
