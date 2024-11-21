package com.prometheus.money.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.res.Res;
import com.prometheus.money.service.IStockInformationService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/stockInformation")
public class StockInformationController {
	@Autowired
	private IStockInformationService stockInformationService;
	@GetMapping("/saveStock")
	public Res<String> uploadImage(){
		stockInformationService.saveStcok();
		return Res.success("拉取数据成功");
	}
}
