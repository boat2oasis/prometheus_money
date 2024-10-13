package com.prometheus.prometheus.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.prometheus.entity.WordsSentence;
import com.prometheus.prometheus.mapper.WordsSentenceMapper;
import com.prometheus.prometheus.res.Res;
import com.zaxxer.hikari.HikariDataSource;

@RestController
@RequestMapping("index")
public class IndexController {
	@Autowired
	private WordsSentenceMapper wordsSentenceMapper;

	@Autowired
	private DataSource dataSource;

	@GetMapping("hello")
	public Res<String> index() {
		HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
		System.out.println("HikariCP 最大连接数: " + hikariDataSource.getMaximumPoolSize());
		System.out.println("HikariCP 最小空闲连接数: " + hikariDataSource.getMinimumIdle());
		System.out.println("HikariCP 连接超时: " + hikariDataSource.getConnectionTimeout());
		WordsSentence word = new WordsSentence();
		word.setExampleSentence("abc");
		word.setWord("abc");
		wordsSentenceMapper.insert(word);
		return null;
	}
}
