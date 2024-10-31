package com.prometheus.money.entity.transfer.re;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeThingsRe {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String imageUrl;

    private LocalDate purchaseDate;

    private BigDecimal price;

    private Byte isCarried;

    private String storageLocation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
