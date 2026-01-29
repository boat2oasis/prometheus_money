package com.prometheus.money.util;

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

import org.springframework.beans.factory.annotation.Autowired;

import com.prometheus.money.entity.StockQuote;
import com.prometheus.money.mapper.StockQuoteMapper;

public class Stockstrape {
	
	@Autowired
	private StockQuoteMapper stockQuoteMapper;
	
	private static final String URL = "https://q.10jqka.com.cn/index/index/board/all/field/ltg/order/asc/page/4/ajax/1/";


	public  void Main(String[] args) {
		try {
			 Document doc = Jsoup.connect(URL)
		                .header("Accept", "text/html, */*; q=0.01")
		                .header("hexin-v", "A0TLl03i8IVHY0TJupfKG-YVFckzXX_SKqH6OF7l0l7S8up3hm04V3qRzSSt")
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
