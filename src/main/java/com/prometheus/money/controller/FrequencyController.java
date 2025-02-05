package com.prometheus.money.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Sentences;
import com.prometheus.money.entity.Similarity;
import com.prometheus.money.entity.transfer.re.DialogueRe;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IFrequencyService;
import com.prometheus.money.service.ISimilarityService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-23
 */
@RestController
@RequestMapping("/frequency")
public class FrequencyController {
	@Autowired
	private IFrequencyService frequencyService;
	
	@Autowired
	private ISimilarityService similarityService;

	@PostMapping("/list")
	public Res<Page<Frequency>> listFrequency(@RequestBody DialogueRe dialogueRe) {
		Integer current = dialogueRe.getCurrent()+1;
		String key = dialogueRe.getKey().replace(" ", "");
		Integer size = dialogueRe.getSize();
		String labelPosition = dialogueRe.getLabelPosition();
		
		Page<Frequency> pages = new Page<Frequency>();
		pages.setCurrent(current);
		pages.setSize(size);

		LambdaQueryWrapper<Frequency> wrapper = new LambdaQueryWrapper<Frequency>();
		if (!StringUtils.isBlank(key)) {
			if (!StringUtils.isBlank(labelPosition)) {
				if ("all".equals(labelPosition)) {
					wrapper.like(Frequency::getWord, key);
				}
				if ("after".equals(labelPosition)) {
					wrapper.likeLeft(Frequency::getWord, key);
				}
				if ("before".equals(labelPosition)) {
					wrapper.likeRight(Frequency::getWord, key);
				}
				if ("central".equals(labelPosition)) {
					wrapper.notLikeRight(Frequency::getWord, key).notLikeLeft(Frequency::getWord, key).like(Frequency::getWord, key);
				}
				if ("similar".equals(labelPosition)) {
					
					LambdaQueryWrapper<Similarity> similaritywrapper = new LambdaQueryWrapper<Similarity>();
					similaritywrapper.eq(Similarity::getFrequencyWord, key);
					List<Similarity> similarityList = similarityService.list(similaritywrapper);
					List<String> worldList = new ArrayList<>();
					for(Similarity si:similarityList) {
						worldList.add(si.getSimilarityWord());
					}
					if(worldList.size()==0) {
						worldList.add(UUID.randomUUID().toString());
					}
					wrapper.in(Frequency::getWord, worldList);
					//wrapper.notLikeRight(Frequency::getWord, key).notLikeLeft(Frequency::getWord, key).like(Frequency::getWord, key);
				}
			}
		}

		Page<Frequency> dialogueList = frequencyService.page(pages, wrapper);
		return Res.success(dialogueList);
	}
}
