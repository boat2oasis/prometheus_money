package com.prometheus.money.entity.transfer.re;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-22
 */

@Getter
@Setter
public class SpentInformationRe implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private String productServiceName;

	private BigDecimal price;

	private Integer quantity;

	private BigDecimal pricees;

	private ZonedDateTime spentDate;

	private Integer category;

	private Integer usedFor;
	
	private Integer accountId;
	
	private Integer necessary;
	
	private BigDecimal couldSave;
}
