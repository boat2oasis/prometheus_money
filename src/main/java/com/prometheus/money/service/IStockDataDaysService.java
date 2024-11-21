package com.prometheus.money.service;

import com.prometheus.money.entity.StockDataDays;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-12
 */
public interface IStockDataDaysService extends IService<StockDataDays> {
	public void fetchStockDataDaysFromEasyMoney() throws InterruptedException;

	public void selectBySzScCyb();
}
