package com.prometheus.prometheus;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.money.PrometheusMoneyApplication;
import com.prometheus.money.entity.ErrInformation;
import com.prometheus.money.entity.LongWord;
import com.prometheus.money.mapper.ErrInformationMapper;
import com.prometheus.money.mapper.LongWordMapper;
@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("dev")
public class LongTest {
	
	@Autowired
	private ErrInformationMapper errInformationMapper;
	
	@Autowired
	private LongWordMapper longMapper;
	@Test
	void main() {
		long start = System.currentTimeMillis();

		int i = 0;
		for (char c = 'a'; c <= 'z'; c++) {
			i = 0;
			System.out.println("=======================================word:" + c);
			while (true) {
				String url = "https://regdict.com/regdict/?n=20&key=" + c + "*&m=" + i * 20;
				//String url ="https://regdict.com/regdict/?n=20&key=c*&m=5900";
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
						
						try{
						System.out.println("pron:" + StringEscapeUtils.unescapeJava(pron));
						
						//pron = pron.replaceAll("\\\\u[0-9a-fA-F]{0,3}$", "");
			            //System.out.println("pron:" + StringEscapeUtils.unescapeJava(pron));
			            
						
						LongWord cipinWord = new LongWord();
			            cipinWord.setFrequencyId(i);
			            //cipinWord.setId(null);
			            cipinWord.setPhoneticUs(StringEscapeUtils.unescapeJava(pron));
			            
			            
			            cipinWord.setWord(word);
			            cipinWord.setFrequencyId(null);
			            cipinWord.setUrl(url);
			            longMapper.insert(cipinWord);
						}
						catch(Exception e) {
							ErrInformation rrrInformation = new ErrInformation();
							rrrInformation.setError(word+":"+pron);
							errInformationMapper.insert(rrrInformation);
						}
						
					}
					if (more) {
						i++;
					} else {
						break;
					}
					// ===== 6. 例句 =====
					
					

				} catch (Exception e) {
					//e.printStackTrace();
					ErrInformation rrrInformation = new ErrInformation();
					rrrInformation.setError(url);
					errInformationMapper.insert(rrrInformation);
					i++;
				}
			}
			System.out.println("=======================================word:" + c);

		}

		i++;
		long end = System.currentTimeMillis();
		System.out.println("一共耗时：" + (end - start));
	}
}
