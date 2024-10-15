package com.prometheus.prometheus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.money.entity.WordsSentence;
import com.prometheus.money.mapper.WordsSentenceMapper;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootTest
class PrometheusApplicationTests {
	@Autowired
	private WordsSentenceMapper wordsSentenceMapper;

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
		System.out.println("HikariCP 最大连接数: " + hikariDataSource.getMaximumPoolSize());
		System.out.println("HikariCP 最小空闲连接数: " + hikariDataSource.getMinimumIdle());
		System.out.println("HikariCP 连接超时: " + hikariDataSource.getConnectionTimeout());
		long startTime = System.currentTimeMillis();
		long oneMinute = 60 * 1000; // 1分钟的毫秒数
		while (System.currentTimeMillis() - startTime < oneMinute) {
			executorService.submit(() -> {
				while (System.currentTimeMillis() - startTime < oneMinute) {

					//System.out.println("活动连接数：" + hikariDataSource.getHikariPoolMXBean().getActiveConnections());
					//System.out.println("空闲连接数：" + hikariDataSource.getHikariPoolMXBean().getIdleConnections());

					WordsSentence word = new WordsSentence();
					word.setExampleSentence("abc");
					word.setWord("abc");
					wordsSentenceMapper.insert(word);
				}
			});
		}
	}
}
