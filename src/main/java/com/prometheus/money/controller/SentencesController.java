package com.prometheus.money.controller;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prometheus.money.entity.Sentences;
import com.prometheus.money.mapper.SentencesMapper;
import com.prometheus.money.res.Res;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/sentences")
public class SentencesController {
	@Autowired
	private SentencesMapper sentencesMapper;
	@GetMapping("/list")
	public Res<Page<Sentences>> list() {
		Page<Sentences> pages = new Page<Sentences>();
		pages.setCurrent(1);
		pages.setPages(100);
		LambdaQueryWrapper<Sentences> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Sentences::getCreatedAt);
		
		Page<Sentences> resultList = sentencesMapper.selectPage(pages,queryWrapper);
		return Res.success(resultList);
	}
	
	@PostMapping("/save")
	public Res<String> save(@RequestBody Sentences sentenceVO) {
		Sentences sentences = new Sentences();
		BeanUtils.copyProperties(sentenceVO, sentences);
		//sentences.setCreatedAt(LocalDateTime.now());
		//sentencesMapper.insert(sentences);
		
		Sentences  sentencesxx = new Sentences();
		if(sentences.getId() != null) {
			sentencesMapper.updateById(sentences);
		}else {
			sentences.setCreatedAt(LocalDateTime.now());
			sentencesMapper.insert(sentences);
		}
		return Res.success("添加成功");
	}
}
