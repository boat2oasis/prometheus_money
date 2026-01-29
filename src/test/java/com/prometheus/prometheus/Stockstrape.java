package com.prometheus.prometheus;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.prometheus.money.PrometheusMoneyApplication;
import com.prometheus.money.entity.StockQuote;
import com.prometheus.money.mapper.StockQuoteMapper;
@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("prod")
public class Stockstrape {
	
	@Autowired
	private StockQuoteMapper stockQuoteMapper;
	
	@Test
	public  void Main() throws InterruptedException {
		for(int j = 10;j<273;j++) {
		try {
			Thread.sleep(3000);
			 String URL = "https://q.10jqka.com.cn/index/index/board/all/field/ltg/order/asc/page/"+j+"/ajax/1/";

			 Document doc = Jsoup.connect(URL)
		                .header("Accept", "text/html, */*; q=0.01")
		                .header("hexin-v", "AwuE-k5_52BKNDs0qcKdnvWgmqTwoB0jmbfjwn0I5MehPyXaBXCvcqmEcy-O")
		                .header("cookie", "u_ukey=A10702B8689642C6BE607730E11E6E4A; u_uver=1.0.0; u_dpass=PLibzTYY17NMpgpydDONT7AbZ3dRX2v%2BQ%2FnZFYD2qz4IelPudeZghlW1DvjCEb1aHi80LrSsTFH9a%2B6rtRvqGg%3D%3D; u_did=95CDDC805C7545F18E39C8E6B6F04431; u_ttype=WEB; ttype=WEB; user=MDpteF81NTg5MzQwNjU6Ok5vbmU6NTAwOjU2ODkzNDA2NTo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDoxNjo6OjU1ODkzNDA2NToxNzU2NTQ2MjgxOjo6MTYwODg3Mjg4MDo2MDQ4MDA6MDoxMDlhZTZkZTM3YjJjOWRkMzc5MjAwYjg2Y2FiMjI4Mzk6ZGVmYXVsdF81OjA%3D; userid=558934065; u_name=mx_558934065; escapename=mx_558934065; ticket=42e05d9f90a3468eb381ca9e361d0c60; user_status=0; utk=680124f59cd5d593949005029b68a3a4; sess_tk=eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6InNlc3NfdGtfMSIsImJ0eSI6InNlc3NfdGsifQ.eyJqdGkiOiIzMThjZWQ5MC1jMmYzLTQ2NzktYmJkNS0zYjA2MDRhYzIyZTUiLCJpYXQiOjE3NTY1NDYyODEsImV4cCI6MTc1NzE1MTA4MSwic3ViIjoiNTU4OTM0MDY1IiwiaXNzIjoidXBhc3MuMTBqcWthLmNvbS5jbiIsImF1ZCI6IjIwMjAxMTE4NTI4ODkwNzIiLCJhY3QiOiJvZmMiLCJjdWhzIjoiMGM2N2VhZDU5ZTU4ZDgzNGRmYjA1NzE3OTA3ZDhhZWUzOTljYWE4ZGFkNTFjODg0YjVlMTViOTJkMmEzZjNlMSJ9.L4r4AHZ2tVsc-cRBO-28IXJ5j7GS7CfMtCw5m8BFFPHY2V78rLXC1evbS6gu7wZUGv79bj_kAszgP5o7wtF4Og; cuc=sjfecimk4u_ukey=A10702B8689642C6BE607730E11E6E4A; u_uver=1.0.0; u_dpass=PLibzTYY17NMpgpydDONT7AbZ3dRX2v%2BQ%2FnZFYD2qz4IelPudeZghlW1DvjCEb1aHi80LrSsTFH9a%2B6rtRvqGg%3D%3D; u_did=95CDDC805C7545F18E39C8E6B6F04431; u_ttype=WEB; ttype=WEB; user=MDpteF81NTg5MzQwNjU6Ok5vbmU6NTAwOjU2ODkzNDA2NTo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDoxNjo6OjU1ODkzNDA2NToxNzU2NTQ2MjgxOjo6MTYwODg3Mjg4MDo2MDQ4MDA6MDoxMDlhZTZkZTM3YjJjOWRkMzc5MjAwYjg2Y2FiMjI4Mzk6ZGVmYXVsdF81OjA%3D; userid=558934065; u_name=mx_558934065; escapename=mx_558934065; ticket=42e05d9f90a3468eb381ca9e361d0c60; user_status=0; utk=680124f59cd5d593949005029b68a3a4; sess_tk=eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6InNlc3NfdGtfMSIsImJ0eSI6InNlc3NfdGsifQ.eyJqdGkiOiIzMThjZWQ5MC1jMmYzLTQ2NzktYmJkNS0zYjA2MDRhYzIyZTUiLCJpYXQiOjE3NTY1NDYyODEsImV4cCI6MTc1NzE1MTA4MSwic3ViIjoiNTU4OTM0MDY1IiwiaXNzIjoidXBhc3MuMTBqcWthLmNvbS5jbiIsImF1ZCI6IjIwMjAxMTE4NTI4ODkwNzIiLCJhY3QiOiJvZmMiLCJjdWhzIjoiMGM2N2VhZDU5ZTU4ZDgzNGRmYjA1NzE3OTA3ZDhhZWUzOTljYWE4ZGFkNTFjODg0YjVlMTViOTJkMmEzZjNlMSJ9.L4r4AHZ2tVsc-cRBO-28IXJ5j7GS7CfMtCw5m8BFFPHY2V78rLXC1evbS6gu7wZUGv79bj_kAszgP5o7wtF4Og; cuc=sjfecimk4")
		                .header("Referer", "https://q.10jqka.com.cn/")
		                .header("sec-ch-ua", "\"Not;A=Brand\";v=\"99\", \"Google Chrome\";v=\"139\", \"Chromium\";v=\"139\"")
		                .header("sec-ch-ua-mobile", "?0")
		                .header("sec-ch-ua-platform", "\"Windows\"")
		                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36")
		                .header("X-Requested-With", "XMLHttpRequest")
		                .timeout(15000)
		                .ignoreContentType(true)  // because it may return JSON or HTML fragment
		                .get();

			Element table = doc.selectFirst("table"); // refine selector if there are multiple tables
			if (table == null) {
				System.out.println("No table found on page.");
				return;
			}

			// Parse header to get column mapping
			Elements headers = table.select("thead tr th");
			Map<Integer, String> colMap = new HashMap<>();
			for (int i = 0; i < headers.size(); i++) {
				String name = headers.get(i).text().trim();
				// map common Chinese headings to our internal names
				switch (name) {
				case "代码":
					colMap.put(i, "stock_code");
					break;
				case "名称":
					colMap.put(i, "stock_name");
					break;
				case "现价":
				case "最新价":
					colMap.put(i, "current_price");
					break;
				case "涨跌幅":
					colMap.put(i, "change_percent");
					break;
				case "涨跌":
					colMap.put(i, "change_amount");
					break;
				case "换手(%)":
				case "换手率":
					colMap.put(i, "turnover_percent");
					break;
				case "量比":
					colMap.put(i, "volume_ratio");
					break;
				case "振幅":
					colMap.put(i, "amplitude_percent");
					break;
				case "成交额":
					colMap.put(i, "turnover_amount");
					break;
				case "流通股":
					colMap.put(i, "circulation_shares");
					break;
				case "流通市值":
					colMap.put(i, "circulation_value");
					break;
				case "市盈率":
					colMap.put(i, "pe_ratio");
					break;
				default:
					colMap.put(i, name); // fallback
				}
			}
			
			
		       List<StockQuote> list = new ArrayList<>();
		       Elements rows = doc.select("tbody tr");
		        for (Element row : rows) {
		            Elements tds = row.select("td");
		            if (tds.size() < 13) continue; // skip invalid rows

		            StockQuote quote = new StockQuote();

		            quote.setId(Integer.parseInt(tds.get(0).text()));               // id
		            quote.setStockCode(tds.get(1).text());                          // stockCode
		            quote.setStockName(tds.get(2).text());                          // stockName
		            quote.setCurrentPrice(parseNumber(tds.get(3).text()));          // currentPrice
		            quote.setChangePercent(parseNumber(tds.get(4).text()));         // changePercent
		            quote.setChangeAmount(parseNumber(tds.get(5).text()));          // changeAmount
		            quote.setSpeedPercent(parseNumber(tds.get(6).text()));          // speedPercent
		            quote.setTurnoverPercent(parseNumber(tds.get(7).text()));       // turnoverPercent
		            quote.setVolumeRatio(parseNumber(tds.get(8).text()));           // volumeRatio
		            quote.setAmplitudePercent(parseNumber(tds.get(9).text()));      // amplitudePercent
		            quote.setTurnoverAmount(parseNumber(tds.get(10).text()));       // turnoverAmount
		            quote.setCirculationShares(parseNumber(tds.get(11).text()));    // circulationShares
		            quote.setCirculationValue(parseNumber(tds.get(12).text()));     // circulationValue
		            quote.setPeRatio(tds.size() > 13 ? tds.get(13).text() : null);  // peRatio
		            quote.setRecordTime(LocalDateTime.now());  
		            
		            stockQuoteMapper.insert(quote);
		            list.add(quote);
		        }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	
	 private static BigDecimal parseNumber(String text) {
	        if (text == null || text.isEmpty() || text.equals("-")) return BigDecimal.ZERO;
	        text = text.replace("%", "").replace(",", "").trim();
	        try {
	            if (text.endsWith("万")) {
	                return new BigDecimal(text.substring(0, text.length() - 1)).multiply(BigDecimal.valueOf(1e4));
	            } else if (text.endsWith("亿")) {
	                return new BigDecimal(text.substring(0, text.length() - 1)).multiply(BigDecimal.valueOf(1e8));
	            }
	            return new BigDecimal(text);
	        } catch (Exception e) {
	            return BigDecimal.ZERO;
	        }
	    }
	
}
