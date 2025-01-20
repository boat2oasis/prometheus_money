package com.prometheus.money.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.prometheus.money.entity.Coca;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Words;
import com.prometheus.money.entity.transfer.vo.WordDo;
import com.prometheus.money.mapper.CocaMapper;
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
		List<Dialogue> dialogueList = dialogueSerivce.list();
		List<Words> wordList = new ArrayList<>();
		for (Dialogue dialogue : dialogueList) {
			// System.out.println(subtitle);
			 String regex = "[^a-zA-Z]+";
			 //String regex = "[ ,]+"
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

					//String regex = ".*\\d.*"; // .* 表示任意字符，\\d 表示数字
					if (/*!Pattern.matches(regex, str) && */!str.isEmpty()) {
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

	//@PostMapping("/import")
	//@GetMapping("/import")
	public Res<String> doExcel(/*@RequestParam(value = "file", required = true) MultipartFile file*/) throws IOException {

		// String filePath = Paths.get("‪D:", "00", "00","coca.xls").toString();
		// String fileName = "‪C:\\Users\\MSI_NB\\Desktop\\coca.xls";
		
		//do Map 
		
		
		Map<String,Frequency> frequencyMap = new HashMap<String,Frequency>();
		Map<String,Coca> cocaMap = new HashMap<String,Coca>();
		
		List<Frequency> frequencyList =  frequencyService.list();
		List<Coca> cocaList =  cocaService.list();
		
		for(Frequency frequency:frequencyList) {
			frequencyMap.put(frequency.getWord(), frequency);
		}
		
		for(Coca coca:cocaList) {
			cocaMap.put(coca.getWord(), coca);
		}
		
		for(Frequency frequency:frequencyList) {
			Coca coca = cocaMap.get(frequency.getWord());
			if(coca != null){
				frequency.setCoca(coca.getSort());
				frequencyService.updateById(frequency);
			}
		}
		
		for(Coca coca:cocaList) {
			Frequency frequency = frequencyMap.get(coca.getWord());
			if(frequency == null){
				Frequency frequencyInsert = new Frequency();
				frequencyInsert.setCoca(coca.getSort());
				frequencyInsert.setFrequency(null);
				frequencyInsert.setWord(coca.getWord());
				frequencyService.save(frequencyInsert);
			}
		}
		
		
		
/*
		if (false) {
			InputStream inputStream = file.getInputStream();
			EasyExcelFactory.read(inputStream, DemoData.class, new ReadListener<DemoData>() {

				@Override
				public void invoke(DemoData user, AnalysisContext analysisContext) {
					System.out.println(user);
					Coca coca = new Coca();
					coca.setWord(user.getWord().toLowerCase());
					coca.setSort(user.getSort());
					cocaMapper.insert(coca);
				}

				@Override
				public void doAfterAllAnalysed(AnalysisContext analysisContext) {

				}
			}).sheet().doRead();
		}
*/
		System.out.println("运行结束");
		return Res.success("返回成功");
	}

	// @GetMapping("/save")
	public Res<String> uploadImage() {
		synchronized (this) {
			if (n == 1) {
				n++;
				System.out.println("n  是 ");
			} else {
				System.out.println("不要重复操作");
				return Res.fail("重复操作");
			}
		}
		for (int i = 1; i <= 6; i++) {

			String rep = "0";
			if (i < 10) {
				rep = rep + i;
			} else {
				rep = i + "";
			}
			// 调用方法解析 ASS 文件
			Integer reason = 2;
			// String spiltText="shad1";
			// String spiltText="4c&H111111&";
			String spiltText = "4c&H000000&";

			String series = "伦敦生活";
			String filePath = Paths.get("D:", "00", "00", "londonlife", "season" + reason, i + ".ass").toString();

			System.out.println(filePath);

			System.out.println(filePath);
			List<String> realSentenceList = new ArrayList<>();
			List<String> realChineseSentenceList = new ArrayList<>();

			extractSubtitlesFromASS(filePath, "UTF-8", realSentenceList, realChineseSentenceList, spiltText); // 指定文件编码
			// 打印所有台词
			for (int j = 0; j < realSentenceList.size(); j++) {
				Dialogue dialogue = new Dialogue();
				if (realSentenceList.size() == 0) {
					System.out.println("Hello World");
				}

				dialogue.setChinese(realChineseSentenceList.get(j));
				dialogue.setReview(true);
				System.out.println(realSentenceList.get(j));
				System.out.println(realChineseSentenceList.get(j));
				dialogue.setSentence(realSentenceList.get(j));
				dialogue.setEpisode(i);
				dialogue.setSeason(reason);
				dialogue.setSeries(series);
				dialogueSerivce.save(dialogue);
			}
		}
		return Res.success("拉取数据成功");
	}

	public static List<String> extractSubtitlesFromASS(String filePath, String charset, List<String> realSentenceList,
			List<String> realChineseSentenceList, String spiltText) {

		String regex = "[\\u4e00-\\u9fa5]";
		Pattern pattern = Pattern.compile(regex);
		boolean inEventsSection = false; // 标记是否进入 [Events] 部分

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), charset))) {
			String line;

			String subTitle = "";
			String sentanceChinese = "";
			String realSentance = "";
			String realSentanceChinese = "";
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// 检测是否进入 [Events] 部分
				if (line.equalsIgnoreCase("[Events]")) {
					inEventsSection = true;
					continue;
				}

				// 如果已经进入 [Events] 部分，解析 Dialogue 行
				if (inEventsSection && line.startsWith("Dialogue:")) {
					// 提取 Dialogue 行中的字幕内容

					String[] parts = line.split(",", 9); // ASS 格式中，Text 字段是第 9 个部分

					if (realSentenceList.size() > 25) {
						System.out.println(parts.length);
					}
					if (parts.length >= 9) {
						String text = parts[8]; // 获取 Text 字段

						if (text.contains(spiltText) && !text.contains("shad0") && !text.contains("fe134")
								&& !text.contains("fad") && !text.contains("an3") && !text.contains("bord0")) {

							subTitle = text.split(spiltText)[1];

							// 获取到当前行的中文和英文
							sentanceChinese = text.split("\\\\N")[0];
							sentanceChinese = sentanceChinese.substring(1, sentanceChinese.length());

							if (realSentance.length() == 0 || realSentance.endsWith(" ")) {
								realSentance = subTitle.substring(1, subTitle.length());

								realSentanceChinese = sentanceChinese;
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

							// if (text.contains("fs38")) {
							// String subTitle = text.split("fs38")[1];
							if (realSentenceList.size() > 25) {
								System.out.println("Hello Wolrd");
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
								System.out.println(realSentance);
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
