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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@ResponseBody
	@PostMapping("dialogue")
	public Res<Page<Dialogue>> search(@RequestBody DialogueRe dialogueRe, HttpServletRequest request) {

		// Page<Dialogue> pageRequest = new Page<>(page, size);

		System.out.println("发送请求===========================");

		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(dialogueRe);
			log.setRequestParam(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
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

		Page<Dialogue> pages = new Page<Dialogue>();
		pages.setCurrent(dialogueRe.getCurrent() == null ? 0 : dialogueRe.getCurrent());
		pages.setSize(dialogueRe.getSize() == null ? 0 : dialogueRe.getSize());
		// String key = "come to think of it";

		String queryString = "{ \"match\": { \"sentence\": { \"query\": \"come to think of it\",\"operator\": \"and\" } } }";

		if (StringUtils.isBlank(dialogueRe.getKey())) {
			queryString = "{ \"match_all\": {} }";
		} else {

		}
		String chinese = extractChinese(dialogueRe.getKey());
		String english = extractEnglish(dialogueRe.getKey());

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
			// Query strictQuery = QueryBuilders.matchPhraseQuery("content", "搜索词").slop(0);
			// // 或 termQuery

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
			HighlightField highlightField = new HighlightField("sentence", highlightParameters); // 替换为你要高亮显示的字段名
			highlightFieldlist.add(highlightField);

			searchEnglish = true;
			// }
		}

		// 动态添加中文条件
		boolean searchChinese = false;

		if (!StringUtils.isBlank(chinese)) {
			HighlightFieldParameters highlightParameters = HighlightFieldParameters.builder()
					.withPreTags("<span style=\"color:#d93025;font-size:16px\">") // 高亮前缀标签
					.withPostTags("</span>") // 高亮后缀标签
					.build();
			HighlightField highlightField = new HighlightField("chinese", highlightParameters); // 替换为你要高亮显示的字段名
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

		// Query query = new CriteriaQueryBuilder(criteria).build();

		Query query2 = new StringQuery(expression);
		query2.setPageable(
				PageRequest.of(dialogueRe.getCurrent() == null ? 0 : dialogueRe.getCurrent(), dialogueRe.getSize()));
		if (highlightFieldlist.size() > 0) {
			query2.setHighlightQuery(highlightQuery);
		}

		SearchHits<Dialogue> searchHits = elasticsearchOperations.search(query2, Dialogue.class);

		for (SearchHit<Dialogue> searchHit : searchHits) {
			List<String> highlightedTexts = searchHit.getHighlightField("sentence"); // 替换为你要高亮显示的字段名
			if (highlightedTexts != null && highlightedTexts.size() > 0) {
				searchHit.getContent().setSentence(highlightedTexts.get(0));
			}
			List<String> highlightedTextsChinese = searchHit.getHighlightField("chinese"); // 替换为你要高亮显示的字段名
			if (highlightedTextsChinese != null && highlightedTextsChinese.size() > 0) {
				searchHit.getContent().setChinese(highlightedTextsChinese.get(0));
			}
		}

		List<Dialogue> resultList = searchHits.stream().map(hit -> hit.getContent()).collect(Collectors.toList());
		pages.setTotal(searchHits.getTotalHits());
		pages.setRecords(resultList);
		return Res.success(pages);
	}

	public static String extractChinese(String text) {
		return extractByRegex(text, "[\\p{IsHan}]+");
	}

	public static String extractEnglish(String text) {
		return extractByRegex(text, "[a-zA-Z0-9]+");
	}

	private static String extractByRegex(String text, String regex) {
		StringBuilder result = new StringBuilder();
		Matcher matcher = Pattern.compile(regex).matcher(text);
		while (matcher.find()) {
			result.append(matcher.group()).append(" ");
		}
		return result.toString().trim();
	}
}
