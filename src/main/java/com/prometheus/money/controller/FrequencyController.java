package com.prometheus.money.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Request;
import com.prometheus.money.entity.Similarity;
import com.prometheus.money.entity.transfer.re.DialogueRe;
import com.prometheus.money.res.LogRecord;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IFrequencyService;
import com.prometheus.money.service.IRequestService;
import com.prometheus.money.service.ISimilarityService;
import com.prometheus.money.util.ClientIpAddress;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-23
 */
@Controller
@RequestMapping("/")
public class FrequencyController {
	@Autowired
	private IFrequencyService frequencyService;

	@Autowired
	private ISimilarityService similarityService;
	@Autowired
	private IRequestService requestService;

	public static Map<String, List<LogRecord>> logMap = new LinkedHashMap<String, List<LogRecord>>();
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@GetMapping("/logMap")
	public Res<Map<String, List<LogRecord>>> logList() {
		return Res.success(logMap);
	}

	@ResponseBody
	@PostMapping("frequency/list")
	public Res<Page<Frequency>> listFrequency(@RequestBody DialogueRe dialogueRe, HttpServletRequest request) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		String uri = request.getRequestURI();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(dialogueRe);
			log.setRequestParam(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.setIp(clientAddress);
		log.setTime(FORMATTER.format(LocalDateTime.now()));
		log.setUri(uri);

		Request requestDatabase = new Request();
		requestDatabase.setIp(clientAddress);
		requestDatabase.setRequestParam(jsonString);
		requestDatabase.setUri(uri);
		requestDatabase.setTime(LocalDateTime.now());
		requestService.save(requestDatabase);

		Integer current = dialogueRe.getCurrent() == null ? 1 : dialogueRe.getCurrent() + 1;
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
					wrapper.notLikeRight(Frequency::getWord, key).notLikeLeft(Frequency::getWord, key)
							.like(Frequency::getWord, key);
				}
				if ("similar".equals(labelPosition)) {

					LambdaQueryWrapper<Similarity> similaritywrapper = new LambdaQueryWrapper<Similarity>();
					similaritywrapper.eq(Similarity::getFrequencyWord, key);
					similaritywrapper.orderByDesc(Similarity::getSimilarity);
					List<Similarity> similarityList = similarityService.list(similaritywrapper);
					List<String> worldList = new ArrayList<>();
					for (Similarity si : similarityList) {
						worldList.add(si.getSimilarityWord());
					}
					if (worldList.size() == 0) {
						worldList.add(UUID.randomUUID().toString());
					}
					wrapper.in(Frequency::getWord, worldList);
					// wrapper.notLikeRight(Frequency::getWord, key).notLikeLeft(Frequency::getWord,
					// key).like(Frequency::getWord, key);
				}
			}
		}
		wrapper.orderByDesc(Frequency::getFrequency).orderByAsc(Frequency::getCoca);
		Page<Frequency> dialogueList = frequencyService.page(pages, wrapper);
		if (!StringUtils.isBlank(key)) {
			String targetChar = "<span style=\"color:#d93025;\">" + key + "</span>";
			for (Frequency frequency : dialogueList.getRecords()) {

				frequency.setWord(frequency.getWord().replace(key, targetChar));
			}
		}
		dialogueList.setCurrent(current - 1);
		return Res.success(dialogueList);
	}

	@GetMapping(value = { "frequency", "frequency/{page}/{size}", "frequency/{page}/{size}/{type}",
			"frequency/{page}/{size}/{type}/{keyword}" })
	public String listFrequencyPage(
			@org.springframework.web.bind.annotation.PathVariable(required = false) Integer page,
			@org.springframework.web.bind.annotation.PathVariable(required = false) Integer size,
			@org.springframework.web.bind.annotation.PathVariable(required = false) String type,
			@org.springframework.web.bind.annotation.PathVariable(required = false) String keyword,
			@org.springframework.web.bind.annotation.RequestParam(value = "keyword", required = false) String reqKeyword,
			@org.springframework.web.bind.annotation.RequestParam(value = "type", required = false) String reqType,
			org.springframework.ui.Model model,
			HttpServletRequest request) {

		// Check for external request
		String referer = request.getHeader("Referer");
		String serverName = request.getServerName();
		boolean isExternal = true;

		if (referer != null && referer.contains(serverName)) {
			isExternal = false;
		}

		model.addAttribute("isExternal", isExternal);

		if (page == null) {
			page = 1;
		}
		if (size == null) {
			size = 10;
		}

		// Priority: Path Variable > Request Param
		String effectiveType = type;
		if (effectiveType == null) {
			effectiveType = reqType;
		}

		String effectiveKeyword = keyword;
		if (effectiveKeyword == null) {
			effectiveKeyword = reqKeyword;
		}

		Page<Frequency> pages = new Page<>(page, size);
		LambdaQueryWrapper<Frequency> wrapper = new LambdaQueryWrapper<>();

		if (!StringUtils.isBlank(effectiveKeyword)) {
			String key = effectiveKeyword.trim().replace(" ", "");
			if (!StringUtils.isBlank(effectiveType)) {
				if ("all".equals(effectiveType) || "general".equals(effectiveType)) {
					wrapper.like(Frequency::getWord, key);
				} else if ("after".equals(effectiveType) || "suffix".equals(effectiveType)) {
					wrapper.likeLeft(Frequency::getWord, key);
				} else if ("before".equals(effectiveType) || "prefix".equals(effectiveType)) {
					wrapper.likeRight(Frequency::getWord, key);
				} else if ("central".equals(effectiveType)) {
					wrapper.notLikeRight(Frequency::getWord, key)
							.notLikeLeft(Frequency::getWord, key)
							.like(Frequency::getWord, key);
				} else if ("similar".equals(effectiveType) || "similarity".equals(effectiveType)) {
					LambdaQueryWrapper<Similarity> similaritywrapper = new LambdaQueryWrapper<>();
					similaritywrapper.eq(Similarity::getFrequencyWord, key);
					similaritywrapper.orderByDesc(Similarity::getSimilarity);
					List<Similarity> similarityList = similarityService.list(similaritywrapper);
					List<String> worldList = new ArrayList<>();
					for (Similarity si : similarityList) {
						worldList.add(si.getSimilarityWord());
					}
					if (worldList.isEmpty()) {
						worldList.add(UUID.randomUUID().toString());
					}
					wrapper.in(Frequency::getWord, worldList);
				}
			} else {
				// Default search if type is missing but keyword exists
				wrapper.like(Frequency::getWord, key);
			}
		}

		wrapper.orderByDesc(Frequency::getFrequency).orderByAsc(Frequency::getCoca);

		Page<Frequency> resultPage = frequencyService.page(pages, wrapper);

		// Highlight keyword
		if (!StringUtils.isBlank(effectiveKeyword)) {
			String key = effectiveKeyword.trim().replace(" ", "");
			String targetChar = "<span style=\"color:#d93025;\">" + key + "</span>";
			for (Frequency frequency : resultPage.getRecords()) {
				if (frequency.getWord() != null) {
					frequency.setWord(frequency.getWord().replace(key, targetChar));
				}
			}
		}

		model.addAttribute("page", resultPage);
		model.addAttribute("keyword", effectiveKeyword);
		model.addAttribute("type", effectiveType);

		return "frequency";
	}
}
