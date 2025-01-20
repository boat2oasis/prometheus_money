package com.prometheus.money.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.FrequencyDialogue;
import com.prometheus.money.entity.transfer.re.DialogueRe;
import com.prometheus.money.mapper.DialogueMapper;
import com.prometheus.money.mapper.FrequencyDialogueMapper;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IDialogueService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
@RestController
@RequestMapping("/dialogue")
public class DialogueController {
	@Autowired
	private IDialogueService dialogueService;
	@Autowired
	private DialogueMapper dialogueMapper;
	@Autowired
	private FrequencyDialogueMapper frequencyDialogueMapper;
	@PostMapping("/list")
	public Res<Page<FrequencyDialogue>> list2(@RequestBody DialogueRe dialogueRe) {
		
		//Page<Dialogue> pageRequest = new Page<>(page, size);
		
		Page<FrequencyDialogue> pages = new Page<FrequencyDialogue>();
		pages.setCurrent(dialogueRe.getCurrent());
		pages.setSize(50);
		String key = dialogueRe.getKey();
	
		 
		 LambdaQueryWrapper<FrequencyDialogue> queryWrapper = new LambdaQueryWrapper<>();
	        // 判断 word 是否为 null 或空字符串
	        if (key != null && !key.trim().isEmpty()) {
	        	queryWrapper.eq(FrequencyDialogue::getFrequencyId, Integer.valueOf(key));
	        }

	        Page<FrequencyDialogue> dialogueList =  this.frequencyDialogueMapper.selectPage(pages, queryWrapper);
        
		 return Res.success(dialogueList);
		
		
		
		//LambdaQueryWrapper<Dialogue> wrapper = new LambdaQueryWrapper<Dialogue>();
		//wrapper.like(Dialogue::getSentence, keys);
		
		//英文,不包含
		

		
		//Page<Dialogue> dialogueList = dialogueService.page(pages,wrapper);
		//return Res.success(dialogueList);
	}

	@PostMapping("/list2")
	public Res<Page<Dialogue>> list(@RequestBody DialogueRe dialogueRe) {
		
		//Page<Dialogue> pageRequest = new Page<>(page, size);
		
		Page<Dialogue> pages = new Page<Dialogue>();
		pages.setCurrent(dialogueRe.getCurrent());
		pages.setSize(50);
		String key = dialogueRe.getKey().replace(" ", "");
	
		 
		 QueryWrapper<Dialogue> queryWrapper = new QueryWrapper<>();
	        // 判断 word 是否为 null 或空字符串
	        if (key != null && !key.trim().isEmpty()) {
	        	//String regexp = "'[[:<:]]"+key+"[[:>:]]'";
	        	//String regexp = "[[:<:]]" + key + "[[:>:]]"; // 移除外层引号
	        	String regexp = "(^|[^a-z0-9])" + key + "([^a-z0-9]|$)";
	        	
	        	
	            queryWrapper.apply("sentence REGEXP {0}",regexp);
	            
	            
	            
	        }

	        Page<Dialogue> dialogueList =  this.dialogueMapper.selectPage(pages, queryWrapper);
        
		 return Res.success(dialogueList);
		
		
		
		//LambdaQueryWrapper<Dialogue> wrapper = new LambdaQueryWrapper<Dialogue>();
		//wrapper.like(Dialogue::getSentence, keys);
		
		//英文,不包含
		

		
		//Page<Dialogue> dialogueList = dialogueService.page(pages,wrapper);
		//return Res.success(dialogueList);
	}
}
