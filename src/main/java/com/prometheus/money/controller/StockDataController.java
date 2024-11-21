package com.prometheus.money.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.res.Res;
import com.prometheus.money.service.IStockDataService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-11
 */
@RestController
@RequestMapping("/stockData")
public class StockDataController {
	@Autowired
	private IStockDataService stockDataService;
	@GetMapping("/get")
	public Res<String> uploadImage(){
		try {
			stockDataService.fetchStockDataFromEasyMoney();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Res.success("拉取数据成功");
	}
}
