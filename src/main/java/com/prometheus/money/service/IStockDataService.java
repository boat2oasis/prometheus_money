package com.prometheus.money.service;

import com.prometheus.money.entity.StockData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-11
 */
public interface IStockDataService extends IService<StockData> {
	
	public void fetchStockDataFromEasyMoney() throws InterruptedException;
}
