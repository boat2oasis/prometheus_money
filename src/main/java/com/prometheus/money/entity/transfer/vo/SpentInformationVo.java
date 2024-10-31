package com.prometheus.money.entity.transfer.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpentInformationVo {
	   private static final long serialVersionUID = 1L;

	    @TableId(value = "id", type = IdType.AUTO)
	    private Integer id;

	    private String productServiceName;

	    private BigDecimal price;

	    private Integer quantity;

	    private BigDecimal pricees;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	    private ZonedDateTime spentDate;

	    private LocalDateTime createAt;

	    private String categoryName;

	    private String usedForName;
	    
	    private Integer type;
	    
	    private Integer accountId;
	    
	    private String accountName;
	    
	    private Integer necessary;
	    
	    private String necessaryName;
	    
	    private BigDecimal couldSave;
	    
	    private Integer operation;
	    
	    private Integer expend =0;
	    
	    private String toStringSpentDate;
	    
	    
}
