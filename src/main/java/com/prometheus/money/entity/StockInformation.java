package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-13
 */
@Getter
@Setter
@TableName("stock_information")
public class StockInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 最新价
     */
    private BigDecimal latestPrice;

    /**
     * 名称
     */
    private String nameSecond;

    /**
     * 市值
     */
    private BigDecimal marketCap;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 类型[1是上证A股，2是深成A股]
     */
    private Integer initialPublicOfferingPlateform;

    /**
     * 英语名称
     */
    private String aEnglishName;

    /**
     * 上市时间
     */
    private LocalDateTime initialPublicOfferingTime;

    /**
     * page in eastmoney
     */
    private Integer page;
}
