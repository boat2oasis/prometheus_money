package com.prometheus.money.service.impl;

import com.prometheus.money.entity.StockData;
import com.prometheus.money.entity.StockDataDays;
import com.prometheus.money.entity.StockInformation;
import com.prometheus.money.mapper.StockDataDaysMapper;
import com.prometheus.money.service.IStockDataDaysService;
import com.prometheus.money.service.IStockInformationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-12
 */
@Service
public class StockDataDaysServiceImpl extends ServiceImpl<StockDataDaysMapper, StockDataDays> implements IStockDataDaysService {

	@Autowired
	private IStockInformationService stockInformationService;
	
	private BigDecimal getBigDecimalOrNull(JSONObject jsonObject, String key) {
		Object value = jsonObject.get(key);
		if (value instanceof String && "-".equals(value)) {
			return null; // or BigDecimal.ZERO, if you prefer a default value
		}
		return jsonObject.getBigDecimal(key);
	}

	private Integer getIntegerOrNull(JSONObject jsonObject, String key) {
		Object value = jsonObject.get(key);
		if (value instanceof String && "-".equals(value)) {
			return null; // or a default integer value if desired
		}
		return jsonObject.getInteger(key);
	}
	
	
	@Override
	public void fetchStockDataDaysFromEasyMoney() throws InterruptedException {
		

		for (int j = 1; j < 105; j++) {
			Thread.sleep(5000);
			String url = "https://push2his.eastmoney.com/api/qt/stock/fflow/daykline/get?"
					+ "cb=jQuery112308976416390129736_1731400125883&lmt=0&klt=101&fields1="
					+ "f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2"
					+ "Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut=b2884a393a59ad64002292a3e90d46a5&secid"
					+ "=1.600519&_=1731400125884"
					+ "484";
			
			https://push2his.eastmoney.com/api/qt/stock/fflow/daykline/get?cb=jQuery112308976416390129736_1731400125883&lmt=0&klt=101&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut=b2884a393a59ad64002292a3e90d46a5&secid=1.200771&_=1731400125884484
			
			try {
				// 使用Jsoup连接并获取文本内容
				String text = Jsoup.connect(url).ignoreContentType(true) // 忽略内容类型
						.execute().body();
				// 输出文本内容
				System.out.println("返回的文本内容：");
			

				// 使用 split 方法，以 "(" 分割字符串，保留分割后的第一个部分
				String result = text.split("\\{")[0];

				// 输出结果
	
				String realVulue = text.substring(result.length(), text.length() - 2);


				JSONObject json = JSON.parseObject(realVulue);
				JSONObject data = (JSONObject) json.get("data");
				JSONArray diff = (JSONArray) data.get("diff");

				List<StockData> stockDataList = new ArrayList<StockData>();

				for (int i = 0; i < diff.size(); i++) {
					// 获取每个元素，强制转换为 JSONObject
					JSONObject jsonObject = diff.getJSONObject(i);

					StockData stockData = new StockData();

					stockData.setCode(jsonObject.getString("f12"));
					stockData.setName(jsonObject.getString("f14"));

					// Helper method to handle possible "-" values
					BigDecimal latestPrice = getBigDecimalOrNull(jsonObject, "f2");
					stockData.setLatestPrice(latestPrice);

					BigDecimal todayMainNetRatio = getBigDecimalOrNull(jsonObject, "f184");
					stockData.setTodayMainNetRatio(todayMainNetRatio);

					Integer todayRank = getIntegerOrNull(jsonObject, "f225");
					stockData.setTodayRank(todayRank);

					BigDecimal todayChange = getBigDecimalOrNull(jsonObject, "f3");
					stockData.setTodayChange(todayChange);

					BigDecimal fiveDayMainNetRatio = getBigDecimalOrNull(jsonObject, "f165");
					stockData.setFiveDayMainNetRatio(fiveDayMainNetRatio);

					Integer fiveDayRank = getIntegerOrNull(jsonObject, "f263");
					stockData.setFiveDayRank(fiveDayRank);

					BigDecimal fiveDayChange = getBigDecimalOrNull(jsonObject, "f109");
					stockData.setFiveDayChange(fiveDayChange);

					BigDecimal tenDayMainNetRatio = getBigDecimalOrNull(jsonObject, "f175");
					stockData.setTenDayMainNetRatio(tenDayMainNetRatio);

					Integer tenDayRank = getIntegerOrNull(jsonObject, "f264");
					stockData.setTenDayRank(tenDayRank);

					BigDecimal tenDayChange = getBigDecimalOrNull(jsonObject, "f160");
					stockData.setTenDayChange(tenDayChange);

					stockData.setSector(jsonObject.getString("f100"));
					
					
					stockData.setRecordDate(LocalDateTime.now());
					

					stockDataList.add(stockData);
	

				}
				//stockDataMapper.insert(stockDataList);
				System.out.println(stockDataList);

			} catch (IOException e) {
				System.err.println("抓取失败：" + e.getMessage());
			}
		}
	
		
		
	}

	@Override
	public void selectBySzScCyb() {
		List<StockInformation> stockInformationList = stockInformationService.list();
		Map<String,StockInformation> stockInformationMap = new HashMap<String,StockInformation>();
		for(StockInformation stockInformation: stockInformationList) {
			stockInformationMap.put(stockInformation.getCode(), stockInformation);
		}
		
		
		for (int j = 1; j < 72; j++) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String url = "https://55.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112400683747695995558_1731496657059&pn="
					+ j
					+ "&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=%7C0%7C0%7C0%7Cweb&fid=f3&fs=m:0+t:80&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1731496657300";
			try {
				String text = Jsoup.connect(url).ignoreContentType(true) // 忽略内容类型
						.execute().body();
				// 输出文本内容
	

				// 使用 split 方法，以 "(" 分割字符串，保留分割后的第一个部分
				String result = text.split("\\{")[0];

				// 输出结果
				
				String realVulue = text.substring(result.length(), text.length() - 2);
			
			
				JSONObject json = JSON.parseObject(realVulue);
				JSONObject data = (JSONObject) json.get("data");
				JSONArray diff = (JSONArray) data.get("diff");
				
				//List<StockData> stockDataList = new ArrayList<StockData>();

				for (int i = 0; i < diff.size(); i++) {
					// 获取每个元素，强制转换为 JSONObject
					JSONObject jsonObject = diff.getJSONObject(i);

					StockData stockData = new StockData();

					stockData.setCode(jsonObject.getString("f12"));
					stockData.setName(jsonObject.getString("f14"));
					
					if(stockInformationMap.get(jsonObject.getString("f12"))!=null){
						StockInformation stockInformation = stockInformationMap.get(jsonObject.getString("f12"));
						stockInformation.setInitialPublicOfferingPlateform(3);
						stockInformationService.updateById(stockInformation);
						stockInformation.setPage(j);
						
					}else {
						StockInformation stockInformation = new StockInformation();
						stockInformation.setCode(jsonObject.getString("f12"));
						stockInformation.setName(jsonObject.getString("f14"));
						stockInformation.setPage(j);
						stockInformation.setInitialPublicOfferingPlateform(3);
						stockInformation.setCreateTime(LocalDateTime.now());
						BigDecimal latestPrice = getBigDecimalOrNull(jsonObject, "f2");
						stockInformation.setLatestPrice(latestPrice);
						stockInformationService.save(stockInformation);
					}
				}
				System.err.println("Page-j是："+j);
				//System.out.println(stockDataList);

			} catch (IOException e) {
				System.err.println("抓取失败：" + e.getMessage()+"j是："+j);
			}
		}
	}
}
