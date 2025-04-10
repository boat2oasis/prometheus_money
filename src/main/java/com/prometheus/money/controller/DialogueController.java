package com.prometheus.money.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.FrequencyDialogue;
import com.prometheus.money.entity.transfer.re.DialogueRe;
import com.prometheus.money.mapper.DialogueMapper;
import com.prometheus.money.mapper.FrequencyDialogueMapper;
import com.prometheus.money.res.LogRecord;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IDialogueService;
import com.prometheus.money.util.ClientIpAddress;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import jakarta.servlet.http.HttpServletRequest;

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
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@PostMapping("/list")
	public Res<Page<FrequencyDialogue>> list2(@RequestBody DialogueRe dialogueRe, HttpServletRequest request) {

		// Page<Dialogue> pageRequest = new Page<>(page, size);

		System.out.println("发送请求===========================");

		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonString = objectMapper.writeValueAsString(dialogueRe);
			log.setRequestParam(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uri = request.getRequestURI();
		log.setIp(clientAddress);
		log.setTime(FORMATTER.format(LocalDateTime.now()));
		log.setUri(uri);
		if (com.prometheus.money.controller.FrequencyController.logMap.get(clientAddress) == null) {
			List<LogRecord> logList = new ArrayList<>();
			logList.add(log);
			com.prometheus.money.controller.FrequencyController.logMap.put(clientAddress, logList);
		} else {
			com.prometheus.money.controller.FrequencyController.logMap.get(clientAddress).add(log);
		}

		Page<FrequencyDialogue> pages = new Page<FrequencyDialogue>();
		pages.setCurrent(dialogueRe.getCurrent() + 1);
		pages.setSize(50);
		String key = dialogueRe.getKey();

		LambdaQueryWrapper<FrequencyDialogue> queryWrapper = new LambdaQueryWrapper<>();
		// 判断 word 是否为 null 或空字符串
		Page<FrequencyDialogue> dialogueList = new Page<>();
		if (key != null && !key.trim().isEmpty()) {
			queryWrapper.eq(FrequencyDialogue::getFrequencyId, Integer.valueOf(key));
			dialogueList = this.frequencyDialogueMapper.selectPage(pages, queryWrapper);
		}
		return Res.success(dialogueList);
	}

	@PostMapping("/search")
	public Res<Page<Dialogue>> search(@RequestBody DialogueRe dialogueRe, HttpServletRequest request) {

		// Page<Dialogue> pageRequest = new Page<>(page, size);

		System.out.println("发送请求===========================");

		String clientAddress = ClientIpAddress.getClientIpAddress();
		LogRecord log = new LogRecord();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonString = objectMapper.writeValueAsString(dialogueRe);
			log.setRequestParam(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uri = request.getRequestURI();
		log.setIp(clientAddress);
		log.setTime(FORMATTER.format(LocalDateTime.now()));
		log.setUri(uri);
		if (com.prometheus.money.controller.FrequencyController.logMap.get(clientAddress) == null) {
			List<LogRecord> logList = new ArrayList<>();
			logList.add(log);
			com.prometheus.money.controller.FrequencyController.logMap.put(clientAddress, logList);
		} else {
			com.prometheus.money.controller.FrequencyController.logMap.get(clientAddress).add(log);
		}

		Page<Dialogue> pages = new Page<Dialogue>();
		pages.setCurrent(dialogueRe.getCurrent()==null?0:dialogueRe.getCurrent());
		pages.setSize(dialogueRe.getSize()==null?0:dialogueRe.getSize());
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
			//Query strictQuery = QueryBuilders.matchPhraseQuery("content", "搜索词").slop(0); // 或 termQuery
			
			
			expression = expression +  """
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
			HighlightField highlightField = new HighlightField("sentence",highlightParameters); // 替换为你要高亮显示的字段名
			highlightFieldlist.add(highlightField);
			

		    		 
			searchEnglish = true;
		//}
		}

		// 动态添加中文条件
		boolean searchChinese = false;
		
		if (!StringUtils.isBlank(chinese)) {
			HighlightFieldParameters highlightParameters = HighlightFieldParameters.builder()
			        .withPreTags("<span style=\"color:#d93025;font-size:16px\">") // 高亮前缀标签
			        .withPostTags("</span>") // 高亮后缀标签
			        .build();
			HighlightField highlightField = new HighlightField("chinese",highlightParameters); // 替换为你要高亮显示的字段名
			highlightFieldlist.add(highlightField);
			String addQuote = "";
			if(searchEnglish) {
				addQuote = ",";
			}
			expression = expression +addQuote+  """
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
		if(!searchChinese  &&  !searchEnglish) {
			expression = """
					               {
    "match_all": {}
  }
					""";
		}

		
	
		
		
		Highlight highlight = new Highlight(highlightFieldlist);
		
		HighlightQuery highlightQuery = new HighlightQuery(highlight, Dialogue.class);
		

		//Query query = new CriteriaQueryBuilder(criteria).build();
		
		
	
		
		Query query2 = new StringQuery(expression);
		query2.setPageable(PageRequest.of(dialogueRe.getCurrent()==null?0:dialogueRe.getCurrent(), dialogueRe.getSize()));
		if(highlightFieldlist.size()>0) {
			query2.setHighlightQuery(highlightQuery);
		}
		
		
		
	
		SearchHits<Dialogue> searchHits = elasticsearchOperations.search(query2, Dialogue.class);
		
		
		for (SearchHit<Dialogue> searchHit : searchHits) {
		    List<String> highlightedTexts = searchHit.getHighlightField("sentence"); // 替换为你要高亮显示的字段名
		    if (highlightedTexts!=null && highlightedTexts.size()>0) {
		    	searchHit.getContent().setSentence(highlightedTexts.get(0));
		    }
		    List<String> highlightedTextsChinese = searchHit.getHighlightField("chinese"); // 替换为你要高亮显示的字段名
		    if (highlightedTextsChinese!=null && highlightedTextsChinese.size()>0) {
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

	@PostMapping("/list2")
	public Res<Page<Dialogue>> list(@RequestBody DialogueRe dialogueRe) {

		// Page<Dialogue> pageRequest = new Page<>(page, size);

		Page<Dialogue> pages = new Page<Dialogue>();
		pages.setCurrent(dialogueRe.getCurrent());
		pages.setSize(50);
		String key = dialogueRe.getKey().replace(" ", "");

		QueryWrapper<Dialogue> queryWrapper = new QueryWrapper<>();
		// 判断 word 是否为 null 或空字符串
		if (key != null && !key.trim().isEmpty()) {
			// String regexp = "'[[:<:]]"+key+"[[:>:]]'";
			// String regexp = "[[:<:]]" + key + "[[:>:]]"; // 移除外层引号
			String regexp = "(^|[^a-z0-9])" + key + "([^a-z0-9]|$)";

			queryWrapper.apply("sentence REGEXP {0}", regexp);

		}

		Page<Dialogue> dialogueList = this.dialogueMapper.selectPage(pages, queryWrapper);

		return Res.success(dialogueList);

		// LambdaQueryWrapper<Dialogue> wrapper = new LambdaQueryWrapper<Dialogue>();
		// wrapper.like(Dialogue::getSentence, keys);

		// 英文,不包含

		// Page<Dialogue> dialogueList = dialogueService.page(pages,wrapper);
		// return Res.success(dialogueList);
	}

	// 增
	@PostMapping
	public Res<String> create() {

		int threadSize = 20;

		List<Dialogue> dialogueList = dialogueService.list();
		CountDownLatch countDownLath = new CountDownLatch(threadSize);
		ExecutorService executor = Executors.newFixedThreadPool(threadSize);
		int size = dialogueList.size();
		int partitionSize = (int) Math.ceil((double) size / threadSize); // 向上取整
		List<List<Dialogue>> result = new ArrayList<>();

		for (int i = 0; i < size; i += partitionSize) {
			int end = Math.min(i + partitionSize, size);
			result.add(new ArrayList<>(dialogueList.subList(i, end)));
		}

		for (List<Dialogue> dialogueListResult : result) {
			executor.submit(() -> {
				List<Dialogue> dialogueListEach = new ArrayList<>();
				for (Dialogue dialogue : dialogueListResult) {
					dialogueListEach.add(dialogue);
					if (dialogueListEach.size() >= 500) {
						System.out.println("数据插入汇众");
						dialogueService.create(dialogueListEach);
						dialogueListEach = new ArrayList<>();
					}
				}
				dialogueService.create(dialogueListEach);
				countDownLath.countDown();
			});
		}
		try {
			countDownLath.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Res.success("操作成功");

	}

	// 查
	@GetMapping("/get")
	public Res<Dialogue> read(Integer id) {
		Dialogue dialogue = dialogueService.read(id);
		return Res.success(dialogue);
	}

	// 改
	@GetMapping("/update")
	public Res<String> update(Integer id) {
		Dialogue dialogue = dialogueService.read(id);
		dialogueService.update(id, dialogue);
		return Res.success("操作成功");
	}

	// 删
	@DeleteMapping("/{id}")
	public Res<String> delete(@PathVariable Integer id) {
		dialogueService.delete(id);
		return Res.success("操作成功");
	}

}
