package com.prometheus.money.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prometheus.money.entity.Coca;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Words;
import com.prometheus.money.entity.transfer.vo.WordDo;
import com.prometheus.money.mapper.CocaMapper;
import com.prometheus.money.mapper.DialogueMapper;
import com.prometheus.money.mapper.FrequencyMapper;
import com.prometheus.money.mapper.WordsMapper;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.ICocaService;
import com.prometheus.money.service.IDialogueService;
import com.prometheus.money.service.IFrequencyService;
import com.prometheus.money.service.IWordsService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/words")
public class WordsController {
	@Autowired
	private IWordsService wordsService;
	@Autowired
	private IDialogueService dialogueSerivce;
	@Autowired
	private WordsMapper wordsMapper;
	@Autowired
	private FrequencyMapper frequencyMapper;
	@Autowired
	private IFrequencyService frequencyService;
	@Autowired
	private CocaMapper cocaMapper;
	@Autowired
	private ICocaService cocaService;
	@Autowired
	private DialogueMapper dialogueMapper;

	private Integer n = 1;

	private List<String> seriesList = new ArrayList<>();

	Map<String, List<Season>> seriesMap = new HashMap<String, List<Season>>();

	@GetMapping("/list")
	public Res<Integer> listData() {

		StoreData();

		List<String> seriesListSearch = new ArrayList<>();
		for (int i = 0; i < seriesList.size(); i++) {
			seriesListSearch.add(seriesList.get(i));
			List<Season> season = seriesMap.get(seriesList.get(i));
			for (int j = 0; i < season.size(); j++) {

			}
		}

		Integer result = wordsMapper.countDistinctWords(seriesList,
				seriesMap.get("摩登家庭").stream().map(Season::getSeasonNumber) // 提取每个 item 的 seasonNumber
						.collect(Collectors.toList()),
				Arrays.asList(1));

		return Res.success(result);
	}

	//@GetMapping("/reSetData")
	public Res<String> reSetData() {
		
		synchronized (this) {
			if (n == 1) {
				n++;
				System.out.println("n  是 ");
			} else {
				System.out.println("不要重复操作");
				return Res.fail("重复操作");
			}
		}
		
		List<Dialogue> dialogueList = dialogueSerivce.list();
		List<Words> wordList = new ArrayList<>();
		for (Dialogue dialogue : dialogueList) {
			// System.out.println(subtitle);
			String regex = "[^a-zA-Z]+";
			// String regex = "[ ,]+"
			String[] arrs = dialogue.getSentence().split(regex);

			for (String str : arrs) {
				if (str != null && str.length() > 0) {
					boolean startWith = true;
					boolean endsWith = true;
					while (str != null && str.length() > 0) {

						boolean startsWithLetter = !str.isEmpty() && ((str.charAt(0) >= 'A' && str.charAt(0) <= 'Z')
								|| (str.charAt(0) >= 'a' && str.charAt(0) <= 'z'));
						if (!startsWithLetter && !str.isEmpty()) {
							str = str.substring(1, str.length());
							startWith = false;
						}

						boolean endsWithLetter = !str.isEmpty() && ((str.charAt(str.length() - 1) >= 'A'
								&& str.charAt(str.length() - 1) <= 'Z')
								|| (str.charAt(str.length() - 1) >= 'a' && str.charAt(str.length() - 1) <= 'z'));

						if (!endsWithLetter && !str.isEmpty()) {
							str = str.substring(0, str.length() - 1);
							startWith = false;
						}

						if (endsWith && startWith) {
							break;
						}
						startWith = true;
						endsWith = true;
					}
					str = str.toLowerCase();

					// String regex = ".*\\d.*"; // .* 表示任意字符，\\d 表示数字
					if (/* !Pattern.matches(regex, str) && */!str.isEmpty()) {
						Words word = new Words();
						word.setEpisode(dialogue.getEpisode());
						word.setSeason(dialogue.getSeason());
						word.setSeries(dialogue.getSeries());
						word.setWord(str);
						// wordsService.save(word);
						wordList.add(word);
						System.out.println(str);
					}
				}
				if (wordList.size() >= 5000) {
					wordsMapper.batchInsert(wordList);
					wordList = new ArrayList<>();
				}
			}
		}
		wordsMapper.batchInsert(wordList);
		/*
		 * StoreData();
		 * 
		 * List<String> seriesListSearch = new ArrayList<>(); for(int
		 * i=0;i<seriesList.size();i++) { seriesListSearch.add(seriesList.get(i));
		 * List<Season> season = seriesMap.get(seriesList.get(i)); for(int
		 * j=0;i<season.size();j++) {
		 * 
		 * } }
		 * 
		 * Integer result =wordsMapper.countDistinctWords(seriesList,
		 * seriesMap.get("摩登家庭").stream() .map(Season::getSeasonNumber) // 提取每个 item 的
		 * seasonNumber .collect(Collectors.toList()), Arrays.asList(1));
		 */
		return Res.success("成功");
	}

	// @GetMapping("/save")
	public Res<String> StoreData() {

		seriesMap.put("摩登家庭",
				Arrays.asList(new Season(1, 24), new Season(2, 23), new Season(3, 24), new Season(4, 24),
						new Season(5, 24), new Season(6, 24), new Season(7, 22), new Season(8, 22), new Season(9, 22),
						new Season(10, 22)));

		seriesMap.put("硅谷", Arrays.asList(new Season(1, 8), new Season(2, 10), new Season(3, 10), new Season(4, 10),
				new Season(5, 8), new Season(6, 7)));

		seriesMap.put("神烦警探", Arrays.asList(new Season(1, 22), new Season(2, 23)));

		seriesMap.put("绝命毒师", Arrays.asList(new Season(1, 7), new Season(2, 13), new Season(3, 13), new Season(4, 13),
				new Season(5, 16)));

		seriesList.addAll(Arrays.asList("摩登家庭", "硅谷", "神烦警探", "绝命毒师"));
		return Res.success(null);
	}

	@GetMapping("/alwaysloveu")
	public Res<String> alwaysloveu() {
		List<Frequency> resultList = frequencyService.list();
		Set<Character> nonEnglishChars = new HashSet<>();
		for (Frequency frquency : resultList) {
			System.out.println(frquency.getWord());
			for (char c : frquency.getWord().toCharArray()) {
				// 如果字符不是小写英文字符，则加入到Set中
				if (!Character.isLowerCase(c)) {
					nonEnglishChars.add(c);

				}
			}
		}
		for (Character c : nonEnglishChars) {
			// System.out.println(c);
		}
		return Res.success("数据返回成功");
	}

	//@GetMapping("/count")
	public Res<String> getWordCount() {
		
		synchronized (this) {
			if (n == 1) {
				n++;
				System.out.println("n  是 ");
			} else {
				System.out.println("不要重复操作");
				return Res.fail("重复操作");
			}
		}
		
		List<WordDo> resultList = wordsMapper.getWordCount();
		Long count = frequencyService.count();
		if (count == 0) {
			List<Frequency> wordList = new ArrayList<>();
			for (WordDo word : resultList) {
				Frequency frequency = new Frequency();
				frequency.setFrequency(word.getFrequency());
				frequency.setWord(word.getWord());
				wordList.add(frequency);
				if (wordList.size() > 500) {
					// frequencyService.save(frequency);
					frequencyMapper.batchInsert(wordList);
					wordList = new ArrayList<>();
				}
			}
			frequencyMapper.batchInsert(wordList);
		}

		return Res.success("数据返回成功");
	}

	@Getter
	@Setter
	@ToString
	public static class DemoData {
		@ExcelProperty("word")
		private String word;
		@ExcelProperty("sort")
		private Integer sort;
	}

	// @PostMapping("/import")
	@GetMapping("/import")
	public Res<String> doExcel(/* @RequestParam(value = "file", required = true) MultipartFile file */)
			throws IOException {

		// String filePath = Paths.get("‪D:", "00", "00","coca.xls").toString();
		// String fileName = "‪C:\\Users\\MSI_NB\\Desktop\\coca.xls";

		// do Map

		Map<String, Frequency> frequencyMap = new HashMap<String, Frequency>();
		Map<String, Coca> cocaMap = new HashMap<String, Coca>();

		List<Frequency> frequencyList = frequencyService.list();
		List<Coca> cocaList = cocaService.list();

		for (Frequency frequency : frequencyList) {
			frequencyMap.put(frequency.getWord(), frequency);
		}

		for (Coca coca : cocaList) {
			cocaMap.put(coca.getWord(), coca);
		}

		for (Frequency frequency : frequencyList) {
			Coca coca = cocaMap.get(frequency.getWord());
			if (coca != null) {
				frequency.setCoca(coca.getSort());
				frequencyService.updateById(frequency);
			}
		}

		for (Coca coca : cocaList) {
		    System.out.println(coca);
			Frequency frequency = frequencyMap.get(coca.getWord());
			if (frequency == null) {
				Frequency frequencyInsert = new Frequency();
				frequencyInsert.setCoca(coca.getSort());
				frequencyInsert.setFrequency(null);
				frequencyInsert.setWord(coca.getWord());
				frequencyService.save(frequencyInsert);
			}
		}

		/*
		 * if (false) { InputStream inputStream = file.getInputStream();
		 * EasyExcelFactory.read(inputStream, DemoData.class, new
		 * ReadListener<DemoData>() {
		 * 
		 * @Override public void invoke(DemoData user, AnalysisContext analysisContext)
		 * { System.out.println(user); Coca coca = new Coca();
		 * coca.setWord(user.getWord().toLowerCase()); coca.setSort(user.getSort());
		 * cocaMapper.insert(coca); }
		 * 
		 * @Override public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		 * 
		 * } }).sheet().doRead(); }
		 */
		System.out.println("运行结束");
		return Res.success("返回成功");
	}

	@Getter
	@Setter
	class Serious {
		private String series;
		private String seriesEn;
		private Integer reason;
		private Integer episode;

		public List<Serious> getSeriousList() {

			List<Serious> seriousList = new ArrayList<>();

			
			// sillcon
			seriousList.add(new Serious("sillcon", "硅谷", 1, 8));
			seriousList.add(new Serious("sillcon", "硅谷", 2, 10));
			seriousList.add(new Serious("sillcon", "硅谷", 3, 10));
			seriousList.add(new Serious("sillcon", "硅谷", 4, 10));
			seriousList.add(new Serious("sillcon", "硅谷", 5, 8));
			seriousList.add(new Serious("sillcon", "硅谷", 6, 7));

			// londonlife
			seriousList.add(new Serious("londonlife", "伦敦生活", 1, 6));
			seriousList.add(new Serious("londonlife", "伦敦生活", 2, 6));

			// itcrowd
			seriousList.add(new Serious("itcrowd", "IT狂人", 1, 6));
			seriousList.add(new Serious("itcrowd", "IT狂人", 2, 6));
			seriousList.add(new Serious("itcrowd", "IT狂人", 3, 6));
			seriousList.add(new Serious("itcrowd", "IT狂人", 4, 6));

			// itcrowd
			seriousList.add(new Serious("breakbad", "绝命毒师", 1, 7));
			seriousList.add(new Serious("breakbad", "绝命毒师", 2, 13));
			seriousList.add(new Serious("breakbad", "绝命毒师", 3, 13));
			seriousList.add(new Serious("breakbad", "绝命毒师", 4, 13));
			seriousList.add(new Serious("breakbad", "绝命毒师", 5, 16));

			// modernfamily
			seriousList.add(new Serious("modernfamily", "摩登家庭", 1, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 2, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 3, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 4, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 5, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 6, 24));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 7, 22));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 8, 22));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 9, 22));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 10, 22));
			seriousList.add(new Serious("modernfamily", "摩登家庭", 11, 18));

			// friend
			seriousList.add(new Serious("friend", "老友记", 1, 24));
			seriousList.add(new Serious("friend", "老友记", 2, 24));
			seriousList.add(new Serious("friend", "老友记", 3, 25));
			seriousList.add(new Serious("friend", "老友记", 4, 24));
			seriousList.add(new Serious("friend", "老友记", 5, 24));
			seriousList.add(new Serious("friend", "老友记", 6, 25));
			seriousList.add(new Serious("friend", "老友记", 7, 24));
			seriousList.add(new Serious("friend", "老友记", 8, 24));
			seriousList.add(new Serious("friend", "老友记", 9, 23));
			seriousList.add(new Serious("friend", "老友记", 10, 17));

			// desperate
			seriousList.add(new Serious("desperate", "绝望主妇", 1, 23));
			seriousList.add(new Serious("desperate", "绝望主妇", 2, 24));
			seriousList.add(new Serious("desperate", "绝望主妇", 3, 23));
			seriousList.add(new Serious("desperate", "绝望主妇", 4, 17));
			seriousList.add(new Serious("desperate", "绝望主妇", 5, 24));
			seriousList.add(new Serious("desperate", "绝望主妇", 6, 23));
			seriousList.add(new Serious("desperate", "绝望主妇", 7, 23));
			seriousList.add(new Serious("desperate", "绝望主妇", 8, 23));

			// theory
			seriousList.add(new Serious("theory", "生活大爆炸", 1, 17));
			seriousList.add(new Serious("theory", "生活大爆炸", 2, 23));
			seriousList.add(new Serious("theory", "生活大爆炸", 3, 23));
			seriousList.add(new Serious("theory", "生活大爆炸", 4, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 5, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 6, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 7, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 8, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 9, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 10, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 11, 24));
			seriousList.add(new Serious("theory", "生活大爆炸", 12, 24));

			// strangers
			seriousList.add(new Serious("strangers", "怪奇物语", 1, 8));
			seriousList.add(new Serious("strangers", "怪奇物语", 2, 9));
			seriousList.add(new Serious("strangers", "怪奇物语", 3, 8));
			seriousList.add(new Serious("strangers", "怪奇物语", 4, 9));

			// saul
			seriousList.add(new Serious("saul", "风骚律师", 1, 10));
			seriousList.add(new Serious("saul", "风骚律师", 2, 10));
			seriousList.add(new Serious("saul", "风骚律师", 3, 10));
			seriousList.add(new Serious("saul", "风骚律师", 4, 10));
			seriousList.add(new Serious("saul", "风骚律师", 5, 10));
			seriousList.add(new Serious("saul", "风骚律师", 6, 13));
			
			
			// shameless
			seriousList.add(new Serious("shameless", "无耻之徒", 1, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 2, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 3, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 4, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 5, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 6, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 7, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 8, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 9, 14));
			seriousList.add(new Serious("shameless", "无耻之徒", 10, 12));
			seriousList.add(new Serious("shameless", "无耻之徒", 11, 12));
			
			return seriousList;

		}

		public Serious() {

		}

		public Serious(String seriesEn, String series, Integer reason, Integer episode) {
			super();
			this.series = series;
			this.seriesEn = seriesEn;
			this.reason = reason;
			this.episode = episode;
		}
	}
	
	class LargeObject {
		private int[] data;

		public LargeObject(int size) {
			this.data = new int[size]; // 创建一个大数组
			for (int i = 0; i < size; i++) {
				this.data[i] = i; // 填充数据
			}
		}

		public int[] getData() {
			return data;
		}
	}
	

	@GetMapping("/save")
	public Res<String> uploadImage() throws UnsupportedEncodingException, FileNotFoundException, IOException {

		
		List<LargeObject> objList = new ArrayList<LargeObject>();
		int p = 0;
		while (p < 1000000000) {
			int size = 100*1024; // 100MB，以 int 为单位 (4 bytes per int)
			objList.add(new LargeObject(size));
			p++;
		}
		for(int j=0;j<p;j++) {
			System.out.println(objList.get(j).getData());
		}
		
		synchronized (this) {
			if (n == 1) {
				n++;
				System.out.println("n  是 ");
			} else {
				System.out.println("不要重复操作");
				// return Res.fail("重复操作");
			}
		}
		long start = System.currentTimeMillis();
		Serious seriousObj = new Serious();
		List<Serious> seriousList = seriousObj.getSeriousList();
		
		//List<Serious> seriousList = new ArrayList<>();
	    //seriousList.add(new Serious("theory", "生活大爆炸", 5, 12));
	    
		String spiltText = "4c&H000000&";

		for (Serious seriousItem : seriousList) {

			Integer reason = seriousItem.getReason();
			String series = seriousItem.getSeries();
			String seriesEn = seriousItem.getSeriesEn();

			//System.out.println(seriousList.size());

			for (int i = 1; i <= seriousItem.getEpisode(); i++) {
				//for (int i = 8; i <= 8; i++) {

				// for (int i = 9; i <= 24; i++) {
				String filePath = Paths.get("D:", "00", "00", seriesEn, "season" + reason, i + ".ass").toString();

				// System.out.println(filePath);

				// System.out.println(filePath);
				List<String> realSentenceList = new ArrayList<>();
				List<String> realChineseSentenceList = new ArrayList<>();

				//读取文件内容
				extractSubtitlesFromASS(filePath, "UTF-16", realSentenceList,realChineseSentenceList, spiltText,seriousItem, i);

				//extractSubtitlesFromASSTest(filePath, "UTF-16", realSentenceList, realChineseSentenceList, spiltText,seriousItem, i); // 指定文件编码

				//修改文件内容
				//extractSubtitlesFromASSff(filePath, "UTF-16", realSentenceList,realChineseSentenceList, spiltText,seriousItem, i); // 指定文件编码

				//System.out.println(filePath);

				List<Dialogue> dialogueList = new ArrayList<>();
				for (int j = 0; j < realSentenceList.size(); j++) {
					/*
					if(j == realSentenceList.size()-1) {
						LambdaQueryWrapper<Dialogue> wrapper = new  LambdaQueryWrapper<Dialogue>();
						wrapper.like(Dialogue::getChinese, realChineseSentenceList.get(j))
						.like(Dialogue::getSentence, realSentenceList.get(j));
						
						List<Dialogue> dialogueLists =dialogueSerivce.list(wrapper);
						if(dialogueLists.size()<1) {
							System.out.println("有bug");
						}
						
					}*/
					
					Dialogue dialogue = new Dialogue();

					dialogue.setChinese(realChineseSentenceList.get(j));
					dialogue.setReview(true);
					//System.out.println(realSentenceList.get(j));
					//System.out.println(realChineseSentenceList.get(j));
					dialogue.setSentence(realSentenceList.get(j));
					dialogue.setEpisode(i);
					dialogue.setSeason(reason);
					dialogue.setSeries(series);
					dialogueList.add(dialogue);
					// dialogueSerivce.save(dialogue);
				}
				/*
				LambdaQueryWrapper<Dialogue> lambdaQueryWrapper = new LambdaQueryWrapper<Dialogue>();
				lambdaQueryWrapper.like(Dialogue::getSentence, realSentenceList.get(realSentenceList.size()-1));
				List<Dialogue> result=  dialogueSerivce.list(lambdaQueryWrapper);
				boolean resulst = true;
				for(Dialogue d:result) {
					if(d.getSeason().intValue() == seriousItem.getReason().intValue() && 
						d.getSeries().equals(seriousItem.getSeries())&& d.getEpisode().intValue() == i) {
						resulst = false;
					}
				}
				if(resulst) {
					System.out.println(seriousItem.getSeries() +":"+ seriousItem.getReason() +":"+ i);
				}*/
			
				
				//dialogueMapper.batchInsert(dialogueList);
			}
		}
		System.out.println("花费时间:" + (System.currentTimeMillis() - start));
		return Res.success("拉取数据成功");
	}

	public static List<String> extractSubtitlesFromASSTest(String filePath, String charset,
			List<String> realSentenceList, List<String> realChineseSentenceList, String spiltText, Serious serious,
			int i) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		List<String> updatedLines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), charset))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split("4c&H000000&");
				if (arr.length > 2) {
					System.out.println(serious.getSeries() + serious.getReason() + i);
					System.out.println(line);
				}
			}
			// 将更新后的行写回文件

		}
		return null;
	}

	public static List<String> extractSubtitlesFromASSff(String filePath, String charset, List<String> realSentenceList,
			List<String> realChineseSentenceList, String spiltText, Serious serious, int i)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		List<String> updatedLines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), charset))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(spiltText)) {
					//line = line.replace("{\\blur3}", "");
					updatedLines.add(line);
				} else {
					System.out.println(line);
				}
			}

			if (updatedLines.size() > 0) {
				Path path = Paths.get(filePath);
				try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_16,
						StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
					for (String updatedLine : updatedLines) {
						writer.write(updatedLine);
						writer.newLine();
					}
				}
			}

			return null;
		}
	}

	
	
	public static List<String> extractSubtitlesFromASS(String filePath, String charset, List<String> realSentenceList,
			List<String> realChineseSentenceList, String spiltText, Serious serious,
			int i) {

		String regex = "[\\u4e00-\\u9fa5]";
		Pattern pattern = Pattern.compile(regex);

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), charset))) {
			String line;

			String subTitle = "";
			String sentanceChinese = "";
			String realSentance = "";
			String realSentanceChinese = "";

			while ((line = reader.readLine()) != null) {
				if (line.contains("He went that way.")) {
					System.out.print("");
				}
				line = line.trim();

				// 如果已经进入 [Events] 部分，解析 Dialogue 行
				if (line.startsWith("Dialogue:")) {
					// 提取 Dialogue 行中的字幕内容

					String[] parts = line.split(",", 9); // ASS 格式中，Text 字段是第 9 个部分

					if (parts.length >= 9) {
						String text = parts[8]; // 获取 Text 字段

						if (text.contains(spiltText)) {

							subTitle = text.split(spiltText)[1];

							// 获取到当前行的中文和英文
							sentanceChinese = text.split("\\\\N")[0];
							sentanceChinese = sentanceChinese.substring(1, sentanceChinese.length());

							if (realSentance.length() == 0 || realSentance.endsWith(" ")) {
								if(text.contains("an8")) {
									System.out.println(serious.getSeries() +":"+ serious.getReason() +":"+ i);
									System.out.println(subTitle);
								}
								Matcher matcher = pattern.matcher(subTitle.substring(1, subTitle.length()));
								if (!matcher.find()) {
									realSentance = subTitle.substring(1, subTitle.length());
									realSentanceChinese = sentanceChinese;
								}else {
									System.out.println(serious.getSeries() +":"+ serious.getReason() +":"+ i);
									System.out.println(subTitle.substring(1, subTitle.length()));
									continue;
								}
							} else {
								// 接上一条英文和中文
								realSentance = realSentance + " " + subTitle.substring(1, subTitle.length());
								realSentanceChinese = realSentanceChinese + sentanceChinese;
							}

							// 去除后面的空格
							while (realSentance.endsWith(" ")) {
								realSentance = realSentance.substring(0, realSentance.length() - 1);
							}

							// 判断最后一个词是否是英文
							boolean endsWithLetter = realSentance.endsWith(",") || realSentance.endsWith("-")
									|| realSentance.endsWith("，")
									|| ((realSentance.charAt(realSentance.length() - 1) >= 'A'
											&& realSentance.charAt(realSentance.length() - 1) <= 'Z')
											|| (realSentance.charAt(realSentance.length() - 1) >= 'a'
													&& realSentance.charAt(realSentance.length() - 1) <= 'z'));
							// 需要加上下一句
							if("Yo, Billie!".equals(realSentance)) {
								System.out.println("Hello World");
							}

							Matcher matcher = pattern.matcher(realSentance);
							if (!matcher.find()) {
								if (!endsWithLetter) {
									if (realSentance.contains("\\N")) {
										realSentenceList.add(realSentance.split("\\\\N")[1]);
										realChineseSentenceList.add(realSentance.split("\\\\N")[0]);
									} else {
										realSentenceList.add(realSentance);
										realChineseSentenceList.add(realSentanceChinese);
									}
									realSentance = "";
									realSentanceChinese = "";
								}
							} else {
							}
						}

					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}

		return null;
	}

	class Season {
		private int seasonNumber;
		private int episodes;

		public Season(int seasonNumber, int episodes) {
			this.seasonNumber = seasonNumber;
			this.episodes = episodes;
		}

		public int getSeasonNumber() {
			return seasonNumber;
		}

		public int getEpisodes() {
			return episodes;
		}
	}
}
