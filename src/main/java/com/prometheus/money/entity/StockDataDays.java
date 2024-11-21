package com.prometheus.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-12
 */
@Getter
@Setter
@TableName("stock_data_days")
public class StockDataDays implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 日期
     */
    private LocalDate recordDate;

    /**
     * 收盘价
     */
    private BigDecimal closingPrice;

    /**
     * 涨跌幅
     */
    private BigDecimal changeRate;

    /**
     * 主力净流入-净额
     */
    private BigDecimal mainNetInflowAmount;

    /**
     * 主力净流入-净占比
     */
    private BigDecimal mainNetInflowRatio;

    /**
     * 超大单净流入-净额
     */
    private BigDecimal largeOrderNetInflowAmount;

    /**
     * 超大单净流入-净占比
     */
    private BigDecimal largeOrderNetInflowRatio;

    /**
     * 大单净流入-净额
     */
    private BigDecimal bigOrderNetInflowAmount;

    /**
     * 大单净流入-净占比
     */
    private BigDecimal bigOrderNetInflowRatio;

    /**
     * 中单净流入-净额
     */
    private BigDecimal mediumOrderNetInflowAmount;

    /**
     * 中单净流入-净占比
     */
    private BigDecimal mediumOrderNetInflowRatio;

    /**
     * 小单净流入-净额
     */
    private BigDecimal smallOrderNetInflowAmount;

    /**
     * 小单净流入-净占比
     */
    private BigDecimal smallOrderNetInflowRatio;
}
