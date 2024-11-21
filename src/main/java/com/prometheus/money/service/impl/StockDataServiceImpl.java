package com.prometheus.money.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prometheus.money.entity.StockData;
import com.prometheus.money.mapper.StockDataMapper;
import com.prometheus.money.service.IStockDataService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-11
 */
@Service
public class StockDataServiceImpl extends ServiceImpl<StockDataMapper, StockData> implements IStockDataService {
	@Autowired
	private StockDataMapper stockDataMapper;

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
	public void fetchStockDataFromEasyMoney() throws InterruptedException {
		for (int j = 1; j < 105; j++) {
			Thread.sleep(5000);
			String url = "https://push2.eastmoney.com/api/qt/clist/get?cb=jQuery112307157377251793295_1731327186175&fid=f184&po=1&pz=50&pn="
					+ j
					+ "&np=1&fltt=2&invt=2&fields=f2%2Cf3%2Cf12%2Cf13%2Cf14%2Cf62%2Cf184%2Cf225%2Cf165%2Cf263%2Cf109%2Cf175%2Cf264%2Cf160%2Cf100%2Cf124%2Cf265%2Cf1&ut=b2884a393a59ad64002292a3e90d46a5&fs=m%3A0%2Bt%3A6%2Bf%3A!2%2Cm%3A0%2Bt%3A13%2Bf%3A!2%2Cm%3A0%2Bt%3A80%2Bf%3A!2%2Cm%3A1%2Bt%3A2%2Bf%3A!2%2Cm%3A1%2Bt%3A23%2Bf%3A!2%2Cm%3A0%2Bt%3A7%2Bf%3A!2%2Cm%3A1%2Bt%3A3%2Bf%3A!2";

			try {
				// 使用Jsoup连接并获取文本内容
				String text = Jsoup.connect(url).ignoreContentType(true) // 忽略内容类型
						.execute().body();
				// 输出文本内容
				System.out.println("返回的文本内容：");
				System.out.println(text);

				// 使用 split 方法，以 "(" 分割字符串，保留分割后的第一个部分
				String result = text.split("\\{")[0];

				// 输出结果
				System.out.println("分割后的字符串: " + result);
				String realVulue = text.substring(result.length(), text.length() - 2);
				System.out.println("==============================");
				System.out.println(realVulue);
				System.out.println("==============================");
				JSONObject json = JSON.parseObject(realVulue);
				JSONObject data = (JSONObject) json.get("data");
				JSONArray diff = (JSONArray) data.get("diff");
				System.out.println(diff);
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
					System.out.println("===============Always Love==============");

				}
				stockDataMapper.insert(stockDataList);
				System.out.println(stockDataList);
				System.out.println(stockDataList);

			} catch (IOException e) {
				System.err.println("抓取失败：" + e.getMessage());
			}
		}
	}
}
