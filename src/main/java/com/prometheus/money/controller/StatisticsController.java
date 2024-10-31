package com.prometheus.money.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.entity.transfer.vo.PriceByCategoryVo;
import com.prometheus.money.entity.transfer.vo.PriceByUsedForVo;
import com.prometheus.money.entity.transfer.vo.SpentInformationVo;
import com.prometheus.money.enums.ProductCategoryEnum;
import com.prometheus.money.enums.ProductPurposeEnum;
import com.prometheus.money.mapper.SpentInformationMapper;
import com.prometheus.money.res.Res;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
	@Autowired
	private SpentInformationMapper spentInformationMapper;

	@GetMapping("/getPriceByUsedForVo")
	public Res<List<PriceByUsedForVo>> getPriceByUsedForVo() {
		List<PriceByUsedForVo>  resultList = spentInformationMapper.getSumPriceByUsedFor();
		for(PriceByUsedForVo price:resultList) {
			price.setUsedForName(ProductPurposeEnum.getValueByCode(price.getUsedFor()));
		}
		return Res.success(resultList);
	}
	@GetMapping("/getSumPriceByCategory")
	public Res<List<PriceByCategoryVo>> getSumPriceByCategory() {
		List<PriceByCategoryVo>  resultList = spentInformationMapper.getSumPriceByCategory();
		for(PriceByCategoryVo price:resultList) {
			price.setCategoryName(ProductCategoryEnum.getValueByCode(price.getCategory()));
		}
		return Res.success(resultList);
	}
	@GetMapping("/getPriceByDays")
	public Res<List<SpentInformationVo>> getPriceByDays() {
		List<SpentInformationVo>  resultList = spentInformationMapper.getSpentInfo();
		Collections.reverse(resultList);
		return Res.success(resultList);
	}
}
