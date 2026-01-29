package com.prometheus.prometheus;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.money.PrometheusMoneyApplication;
import com.prometheus.money.entity.CipinWord;
import com.prometheus.money.entity.ErrInformation;
import com.prometheus.money.mapper.CipinWordMapper;
import com.prometheus.money.mapper.ErrInformationMapper;

@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("dev")
public class CipinCrawler {
	@Autowired
	private CipinWordMapper cipinWordMapper;
	

	@Autowired
	private ErrInformationMapper errInformationMapper;
	
	@Test
      void main() {
    	long start = System.currentTimeMillis();
    	
    	for(int i=1;i<18000;i++) {
        String url = "https://www.cipindanci.com/word.php?id="+i;

      
        try {
            // ===== 1. 发起 HTTP 请求 =====
            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true) // 关键：返回的是 JSON
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .execute();

            String json = response.body();

            // ===== 2. 解析 JSON =====
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            // ===== 3. 基本信息 =====
            String chinese = root.get(0).get(0).get(0).asText();
            String english = root.get(0).get(0).get(1).asText();
            String pinyin = root.get(0).get(1).get(2).asText();
            String phonetic = root.get(0).get(1).get(3).asText();
            
            CipinWord cipinWord = new CipinWord();
            cipinWord.setFrequencyId(i);
            //cipinWord.setId(null);
            cipinWord.setPhoneticCipin(phonetic);
            cipinWord.setWord(english);
            cipinWordMapper.insert(cipinWord);
            

            System.out.println("====== 词条 ======");
            System.out.println("英文: "+i+":" + english);
         
       

            // ===== 6. 例句 =====
            System.out.println("\n====== 例句 ======");
      

        } catch (Exception e) {
        	ErrInformation rrrInformation = new ErrInformation();
			rrrInformation.setError(url);
			errInformationMapper.insert(rrrInformation);
        }
    }
    	long end = System.currentTimeMillis();
    	System.out.println("一共耗时："+(end-start));
    }
}
