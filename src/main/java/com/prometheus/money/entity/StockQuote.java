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
 * @since 2025-08-30
 */
@Getter
@Setter
@TableName("stock_quote")
public class StockQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 代码
     */
    private String stockCode;

    /**
     * 名称
     */
    private String stockName;

    /**
     * 现价
     */
    private BigDecimal currentPrice;

    /**
     * 涨跌幅(%)
     */
    private BigDecimal changePercent;

    /**
     * 涨跌
     */
    private BigDecimal changeAmount;

    /**
     * 涨速(%)
     */
    private BigDecimal speedPercent;

    /**
     * 换手(%)
     */
    private BigDecimal turnoverPercent;

    /**
     * 量比
     */
    private BigDecimal volumeRatio;

    /**
     * 振幅(%)
     */
    private BigDecimal amplitudePercent;

    /**
     * 成交额(元)
     */
    private BigDecimal turnoverAmount;

    /**
     * 流通股(股)
     */
    private BigDecimal circulationShares;

    /**
     * 流通市值(元)
     */
    private BigDecimal circulationValue;

    /**
     * 市盈率(可能为数字或亏损)
     */
    private String peRatio;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
}
