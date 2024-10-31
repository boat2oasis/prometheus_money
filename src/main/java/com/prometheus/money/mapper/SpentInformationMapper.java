package com.prometheus.money.mapper;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prometheus.money.entity.SpentInformation;
import com.prometheus.money.entity.transfer.vo.PriceByCategoryVo;
import com.prometheus.money.entity.transfer.vo.PriceByUsedForVo;
import com.prometheus.money.entity.transfer.vo.SpentInformationVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @param <PriceByCategoryVo>
 * @since 2024-10-22
 */
public interface SpentInformationMapper extends BaseMapper<SpentInformation> {
	List<SpentInformationVo> getSpentInfo();

	BigDecimal getTotalSpent(ZonedDateTime startDate, ZonedDateTime endDate);
	
	BigDecimal getCouldSave(ZonedDateTime startDate, ZonedDateTime endDate);


	List<PriceByUsedForVo> getSumPriceByUsedFor();

	List<PriceByCategoryVo> getSumPriceByCategory();
}
