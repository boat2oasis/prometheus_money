package com.prometheus.money.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.Request;
import com.prometheus.money.entity.transfer.re.DialogueRe;
import com.prometheus.money.mapper.DialogueMapper;
import com.prometheus.money.mapper.FrequencyDialogueMapper;
import com.prometheus.money.res.LogRecord;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IDialogueService;
import com.prometheus.money.service.IRequestService;
import com.prometheus.money.util.ClientIpAddress;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
@Controller
@RequestMapping("/")
public class DialogueController {
	@Autowired
	private IDialogueService dialogueService;
	@Autowired
	private DialogueMapper dialogueMapper;
	@Autowired
	private FrequencyDialogueMapper frequencyDialogueMapper;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	@Autowired
	private IRequestService requestService;

	@GetMapping(value = { "dialogue", "dialogue/{page}/{size}", "dialogue/{page}/{size}/{keyword}" })
	public String dialogue(Model model,
			@org.springframework.web.bind.annotation.PathVariable(required = false) Integer page,
			@org.springframework.web.bind.annotation.PathVariable(required = false) Integer size,
			@org.springframework.web.bind.annotation.PathVariable(required = false) String keyword,
			@org.springframework.web.bind.annotation.RequestParam(value = "page", required = false) Integer reqPage,
			@org.springframework.web.bind.annotation.RequestParam(value = "size", required = false) Integer reqSize,
			@org.springframework.web.bind.annotation.RequestParam(value = "keyword", required = false) String reqKeyword,
			HttpServletRequest request) {

		if (page == null) {
			page = reqPage != null ? reqPage : 1;
		}
		if (size == null) {
			size = reqSize != null ? reqSize : 10;
		}
		String effectiveKeyword = keyword;
		if (effectiveKeyword == null) {
			effectiveKeyword = reqKeyword;
		}
		if (effectiveKeyword != null) {
			effectiveKeyword = effectiveKeyword.replace("-", " ");
		}

		// Log the request (similar to search method)
		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		String uri = request.getRequestURI();
		log.setIp(clientAddress);
		log.setTime(FORMATTER.format(LocalDateTime.now()));
		log.setUri(uri);
		log.setRequestParam("page=" + page + ", size=" + size + ", keyword=" + effectiveKeyword);

		Request requestDatabase = new Request();
		requestDatabase.setIp(clientAddress);
		requestDatabase.setRequestParam(log.getRequestParam());
		requestDatabase.setUri(uri);
		requestDatabase.setTime(LocalDateTime.now());
		requestService.save(requestDatabase);

		Page<Dialogue> resultPage = executeSearch(effectiveKeyword, page, size);

		model.addAttribute("page", resultPage);
		model.addAttribute("keyword", effectiveKeyword);

		return "dialogue";
	}

	@ResponseBody
	@PostMapping("dialogue")
	public Res<Page<Dialogue>> search(@RequestBody DialogueRe dialogueRe, HttpServletRequest request) {
		// Log the request
		System.out.println("发送请求===========================");

		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(dialogueRe);
			log.setRequestParam(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String uri = request.getRequestURI();
		log.setIp(clientAddress);
		log.setTime(FORMATTER.format(LocalDateTime.now()));
		log.setUri(uri);

		Request requestDatabase = new Request();
		requestDatabase.setIp(clientAddress);
		requestDatabase.setRequestParam(jsonString);
		requestDatabase.setUri(uri);
		requestDatabase.setTime(LocalDateTime.now());
		requestService.save(requestDatabase);

		int page = dialogueRe.getCurrent() == null ? 1 : dialogueRe.getCurrent(); // Default to 1 if null, logic inside
																					// executeSearch handles 0-index
																					// conversion
		int size = dialogueRe.getSize() == null ? 10 : dialogueRe.getSize();
		// If the incoming request uses 0 for the first page, we might need to adjust,
		// but typically we standardize on implementation.
		// If current legacy uses 0-based, executeSearch taking 1-based might break it
		// if we don't adjust.
		// Assuming legacy calls might send 0.
		if (page == 0)
			page = 1;

		Page<Dialogue> pages = executeSearch(dialogueRe.getKey(), page, size);
		return Res.success(pages);
	}

	private Page<Dialogue> executeSearch(String keyword, int page, int size) {
		Page<Dialogue> pages = new Page<>();
		pages.setCurrent(page);
		pages.setSize(size);

		String queryString = "{ \"match\": { \"sentence\": { \"query\": \"come to think of it\",\"operator\": \"and\" } } }";

		if (StringUtils.isBlank(keyword)) {
			queryString = "{ \"match_all\": {} }";
		}

		String chinese = extractChinese(keyword);
		String english = extractEnglish(keyword);

		List<HighlightField> highlightFieldlist = new ArrayList<>();

		// 动态添加英文条件
		String expression = """
				     {
				       "bool": {
				           "must": [
				""";

		String highLightQuery = expression;
		boolean searchEnglish = false;

		if (!StringUtils.isBlank(english)) {
			expression = expression + """
					  {
					"match_phrase": {
					              "sentence": {
					                  "query": "%s",
					                  "slop": 100
					              }
					          }
					          }
					         """.formatted(english);

			highLightQuery = """
					          {
					"match": {
					                   "sentence": {
					                       "query": "%s",
					                       "operator": "and"
					                   }
					               }
					               }
					           """.formatted(english);

			HighlightFieldParameters highlightParameters = HighlightFieldParameters.builder()
					.withPreTags("<span style=\"color:#d93025;font-size:16px\">") // 高亮前缀标签
					.withPostTags("</span>") // 高亮后缀标签
					.withHighlightQuery(new StringQuery(highLightQuery))
					.build();
			HighlightField highlightField = new HighlightField("sentence", highlightParameters);
			highlightFieldlist.add(highlightField);

			searchEnglish = true;
		}

		// 动态添加中文条件
		boolean searchChinese = false;

		if (!StringUtils.isBlank(chinese)) {
			HighlightFieldParameters highlightParameters = HighlightFieldParameters.builder()
					.withPreTags("<span style=\"color:#d93025;font-size:16px\">") // 高亮前缀标签
					.withPostTags("</span>") // 高亮后缀标签
					.build();
			HighlightField highlightField = new HighlightField("chinese", highlightParameters);
			highlightFieldlist.add(highlightField);
			String addQuote = "";
			if (searchEnglish) {
				addQuote = ",";
			}
			expression = expression + addQuote + """
					{
					"match": {
					                   "chinese": {
					                       "query": "%s",
					                       "operator": "and"
					                   }
					               }
					               }
					         """.formatted(chinese);
			searchChinese = true;
		}

		expression = expression + """
				                ]
				            }
				        }
				""";
		if (!searchChinese && !searchEnglish) {
			expression = """
					                           {
					  "match_all": {}
					}
					            """;
		}

		Highlight highlight = new Highlight(highlightFieldlist);
		HighlightQuery highlightQuery = new HighlightQuery(highlight, Dialogue.class);

		// Calculate 0-based page index for Elasticsearch
		int pageIndex = (page > 0) ? page - 1 : 0;

		Query query2 = new StringQuery(expression);
		query2.setPageable(PageRequest.of(pageIndex, size));
		if (highlightFieldlist.size() > 0) {
			query2.setHighlightQuery(highlightQuery);
		}

		SearchHits<Dialogue> searchHits = elasticsearchOperations.search(query2, Dialogue.class);

		for (SearchHit<Dialogue> searchHit : searchHits) {
			List<String> highlightedTexts = searchHit.getHighlightField("sentence");
			if (highlightedTexts != null && highlightedTexts.size() > 0) {
				searchHit.getContent().setSentence(highlightedTexts.get(0));
			}
			List<String> highlightedTextsChinese = searchHit.getHighlightField("chinese");
			if (highlightedTextsChinese != null && highlightedTextsChinese.size() > 0) {
				searchHit.getContent().setChinese(highlightedTextsChinese.get(0));
			}
		}

		List<Dialogue> resultList = searchHits.stream().map(hit -> hit.getContent()).collect(Collectors.toList());
		pages.setTotal(searchHits.getTotalHits());
		pages.setRecords(resultList);
		return pages;
	}

	public static String extractChinese(String text) {
		return extractByRegex(text, "[\\p{IsHan}]+");
	}

	public static String extractEnglish(String text) {
		return extractByRegex(text, "[a-zA-Z0-9]+");
	}

	private static String extractByRegex(String text, String regex) {
		if (text == null) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		Matcher matcher = Pattern.compile(regex).matcher(text);
		while (matcher.find()) {
			result.append(matcher.group()).append(" ");
		}
		return result.toString().trim();
	}
}
