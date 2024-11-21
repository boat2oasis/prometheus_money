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
 * @since 2024-11-12
 */
@Getter
@Setter
@TableName("stock_data")
public class StockData implements Serializable {

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
     * 今日 主力净占比
     */
    private BigDecimal todayMainNetRatio;

    /**
     * 今日 排名
     */
    private Integer todayRank;

    /**
     * 今日 涨跌
     */
    private BigDecimal todayChange;

    /**
     * 5日 主力净占比
     */
    private BigDecimal fiveDayMainNetRatio;

    /**
     * 5日 排名
     */
    private Integer fiveDayRank;

    /**
     * 5日 涨跌
     */
    private BigDecimal fiveDayChange;

    /**
     * 10日 主力净占比
     */
    private BigDecimal tenDayMainNetRatio;

    /**
     * 10日 排名
     */
    private Integer tenDayRank;

    /**
     * 10日 涨跌
     */
    private BigDecimal tenDayChange;

    /**
     * 所属板块
     */
    private String sector;

    /**
     * 日期
     */
    private LocalDateTime recordDate;
}
