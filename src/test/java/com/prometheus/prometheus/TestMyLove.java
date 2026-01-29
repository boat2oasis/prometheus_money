package com.prometheus.prometheus;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.prometheus.money.PrometheusMoneyApplication;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("dev")
@Slf4j
public class TestMyLove {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void setKey() throws IOException {
		
	
	
		
		Runnable calsl =() -> {
			System.out.println("HELLO WORLD");
			
		}; 
		
		
		 ExecutorService executor = Executors.newSingleThreadExecutor();

	    

		
		
		long startTime = System.nanoTime();
		
		FileOutputStream fos = new FileOutputStream("runoob.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
		
		for(int i=1000000;i<1500000;i++) {
			try {
				 out.write(String.valueOf(i));
			     out.flush();
			     fos.getFD().sync();
			     out.newLine();
			     
				redisTemplate.opsForValue().set(i+"", i);
			}catch(Exception e) {
				e.printStackTrace();
				out.close();
			}
			log.info(String.valueOf(i));
		}
		out.close();
		long endTime = System.nanoTime();
		long duration = endTime-startTime;
		System.out.println("程序运行时间: " + (duration / 1_000_000_000.0) + " 秒");
				
	}
}
