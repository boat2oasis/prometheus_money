package com.prometheus.money.entity.excel.listener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.prometheus.money.entity.StockInformation;
import com.prometheus.money.entity.excel.entity.SS;
import com.prometheus.money.service.IStockInformationService;

public class DataModelListener extends AnalysisEventListener<SS> {
	private final IStockInformationService stockInformationService;
	
	private Map<String,StockInformation> stockInformationMap = new HashMap<>();

    // 注入 Mapper
    public DataModelListener(IStockInformationService stockInformationService) {
        this.stockInformationService = stockInformationService;
    }
    
    @Override
    public void invoke(SS dataModel, AnalysisContext context) {
        // Handle each row, e.g., save to database or process in memory
    	
    	List<StockInformation> list = stockInformationService.list();
    	for(StockInformation stockInformation:list) {
    		stockInformationMap.put(stockInformation.getCode(), stockInformation);
    	}
    	
    	if(stockInformationMap.get(dataModel.getAStockCode())!=null) {
    		StockInformation stockInformation = stockInformationMap.get(dataModel.getAStockCode());
    		stockInformation.setAEnglishName(dataModel.getAEnglishName());
    	
    		// 定义日期格式
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            
            // 将字符串解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dataModel.getInitialPublicOfferingTime(), dateFormatter);
            
            // 将 LocalDate 转为 LocalDateTime
            LocalDateTime localDateTime = localDate.atStartOfDay();
    		//stockInformation.setCreateTime(LocalDateTime.now());
    		
    		stockInformation.setInitialPublicOfferingPlateform(1);
    		stockInformation.setInitialPublicOfferingTime(localDateTime);
    		if(!stockInformation.getName().equals(dataModel.getASimpleName())) {
    			stockInformation.setNameSecond(dataModel.getASimpleName());
    		}
    		stockInformationService.updateById(stockInformation);
    	}else {
    		StockInformation information = new StockInformation();
    		information.setCode(dataModel.getAStockCode());
    		information.setName(dataModel.getASimpleName());
    		information.setAEnglishName(dataModel.getAEnglishName());
    		information.setInitialPublicOfferingPlateform(1);
    		// 定义日期格式
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            
            // 将字符串解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dataModel.getInitialPublicOfferingTime(), dateFormatter);
            
            // 将 LocalDate 转为 LocalDateTime
            LocalDateTime localDateTime = localDate.atStartOfDay();
    		information.setInitialPublicOfferingTime(localDateTime);
    		information.setCreateTime(LocalDateTime.now());
    		stockInformationService.save(information);
    		
    	}
    	
  
    	
        System.out.println("Read row: " + dataModel.toString());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // Do any final processing if needed
        System.out.println("All data read");
    }
}
