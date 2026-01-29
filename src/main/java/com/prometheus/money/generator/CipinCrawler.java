package com.prometheus.money.generator;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CipinCrawler {
	// CipinWord
	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		int i = 0;
		for (char c = 'a'; c <= 'z'; c++) {
			i = 0;
			System.out.println("=======================================word:" + c);
			while (true) {
				String url = "https://regdict.com/regdict/?n=20&key="+c+"*&m=" + i * 20;
				try {
					// ===== 1. 发起 HTTP 请求 =====
					Connection.Response response = Jsoup.connect(url).ignoreContentType(true) // 关键：返回的是 JSON
							.userAgent("Mozilla/5.0").timeout(10_000).execute();

					String json = response.body();

					// ===== 2. 解析 JSON =====
					ObjectMapper mapper = new ObjectMapper();
					JsonNode root = mapper.readTree(json);

					JsonNode wordsNode = root.get("words");
					Boolean more = root.get("more").asBoolean();

					for (JsonNode wordNode : wordsNode) {
						String word = wordNode.get("word").asText();
						String pron = wordNode.get("us_pron").asText();
						System.out.println("word:" + word);
						System.out.println("pron:" + StringEscapeUtils.unescapeJava(pron));
					}
					if (more) {
						i++;
					} else {
						break;
					}
					// ===== 6. 例句 =====

				} catch (Exception e) {
					System.out.println(url);
				}
			}
			System.out.println("=======================================word:" + c);

		}

		i++;
		long end = System.currentTimeMillis();
		System.out.println("一共耗时：" + (end - start));
	}
}
