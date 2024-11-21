package com.prometheus.money.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.prometheus.money.entity.excel.entity.SS;
import com.prometheus.money.entity.excel.listener.DataModelListener;
import com.prometheus.money.service.IStockDataDaysService;
import com.prometheus.money.service.IStockInformationService;

@RestController
@RequestMapping("/excel")
public class ExcelController {
	@Autowired
	private IStockInformationService stockInformationService;
	
	@Autowired
	private IStockDataDaysService stockDataDaysService;

	@PostMapping("/upload")
	public String uploadExcel(@RequestParam("file") MultipartFile file) {
		try {
			EasyExcel.read(file.getInputStream(), SS.class, new DataModelListener(stockInformationService)).sheet()
					.doRead();
			return "Upload successful!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to upload Excel file!";
		}
	}

	@GetMapping("/loadStockInformation")
	public String loadStockInformation() {
		stockDataDaysService.selectBySzScCyb();
		return null;
	}

}
