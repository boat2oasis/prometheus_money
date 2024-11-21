package com.prometheus.money.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prometheus.money.entity.StockData;
import com.prometheus.money.entity.StockInformation;
import com.prometheus.money.mapper.StockInformationMapper;
import com.prometheus.money.service.IStockDataService;
import com.prometheus.money.service.IStockInformationService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-12
 */
@Service
public class StockInformationServiceImpl extends ServiceImpl<StockInformationMapper, StockInformation> implements IStockInformationService {
	@Autowired
	private IStockDataService stockDataService;
	

	@Override
	public void saveStcok() {
		List<StockData> stockDataList = stockDataService.list();
		for(StockData stockData:stockDataList) {
			stockData.getId();
			stockData.getCode();
			stockData.getName();
			stockData.getLatestPrice();
			
			StockInformation information = new StockInformation();
			information.setCode(stockData.getCode());
			information.setLatestPrice(stockData.getLatestPrice());
			information.setName(stockData.getName());
			
			this.save(information);
		}
	}
}
