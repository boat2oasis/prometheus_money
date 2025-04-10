package com.prometheus.prometheus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.prometheus.money.PrometheusMoneyApplication;
import com.prometheus.money.entity.Coca;
import com.prometheus.money.entity.Dialogue;
import com.prometheus.money.entity.Enwords;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.FrequencyDialogue;
import com.prometheus.money.entity.Similarity;
import com.prometheus.money.mapper.FrequencyDialogueMapper;
import com.prometheus.money.mapper.FrequencyMapper;
import com.prometheus.money.mapper.SimilarityMapper;
import com.prometheus.money.service.ICocaService;
import com.prometheus.money.service.IDialogueService;
import com.prometheus.money.service.IEnwordsService;
import com.prometheus.money.service.IFrequencyDialogueService;
import com.prometheus.money.service.IFrequencyService;
import com.prometheus.money.service.ISimilarityService;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;

@SpringBootTest(classes = PrometheusMoneyApplication.class)
@ActiveProfiles("dev")
public class FrequencyTest {
	@Autowired
	private IFrequencyService frequencyService;
	@Autowired
	private IEnwordsService enwordsService;
	@Autowired
	private ICocaService cocoService;
	@Autowired
	private IDialogueService dialogueService;
	@Autowired
	private IFrequencyDialogueService frequencyDialogueService;
	@Autowired
	private FrequencyMapper frequencyMapper;
	@Autowired
	private FrequencyDialogueMapper frequencyDialogueMapper;
	@Autowired
	private ISimilarityService similarityService;
	@Autowired
	private SimilarityMapper similarityMapper;

	@Test
	void Dothing() {
		List<Frequency> frequencyList = frequencyService.list();
		List<Dialogue> dialogueList = dialogueService.list();

		List<FrequencyDialogue> batchInsert = new ArrayList<FrequencyDialogue>();

		for (Frequency frequency : frequencyList) {
			System.out.println(frequency.getId());
			String word = frequency.getWord().toLowerCase();
			String regex = "\\b" + word + "\\b";
			Pattern pattern = Pattern.compile(regex);
			for (Dialogue dialogue : dialogueList) {
				String sentence = dialogue.getSentence();
				// 匹配完整单词
				if (sentence.toLowerCase().contains(word)) {
					Matcher matcher = pattern.matcher(sentence.toLowerCase());
					boolean containsWord = matcher.find();
					if (containsWord) {
						FrequencyDialogue frequencyDialogue = new FrequencyDialogue();
						BeanUtils.copyProperties(dialogue, frequencyDialogue);
						frequencyDialogue.setFrequencyId(frequency.getId());
						frequencyDialogue.setId(null);
						// frequencyDialogueService.batchin(frequencyDialogue);
						batchInsert.add(frequencyDialogue);
						;
					}
				}
				if (batchInsert.size() >= 5000) {
					// frequencyDialogueMapper.batchInsert(batchInsert);
					batchInsert = new ArrayList<>();
				}
			}
		}
		// frequencyDialogueMapper.batchInsert(batchInsert);
	}
	@Test
	void similarity() {
		Instant start = Instant.now();
		
		CountDownLatch countDownLath = new CountDownLatch(20);
		List<Frequency> frequencyList = frequencyService.list();
		System.out.println("=============="+frequencyList.size());
		JaroWinkler jw = new JaroWinkler();
		Levenshtein levenshtein = new Levenshtein();

		ExecutorService executor = Executors.newFixedThreadPool(20);

		int size = frequencyList.size();
		int partitionSize = (int) Math.ceil((double) size / 20); // 向上取整
		List<List<Frequency>> result = new ArrayList<>();

		
		for (int i = 0; i < size; i += partitionSize) {
			int end = Math.min(i + partitionSize, size);
			result.add(new ArrayList<>(frequencyList.subList(i, end)));
		}
		//result.add(new ArrayList<>(frequencyList.subList(0, frequencyList.size())));
		
		System.out.println("=============="+result.size());
		System.out.println("=============="+result.get(0).size());

		for (List<Frequency> frequencyEachdList : result) {

			executor.submit(() -> {
				List<Similarity> similarityList = new ArrayList<>();
				for (Frequency frequency : frequencyEachdList) {
					//System.out.println(frequency.getId());
					//System.out.println("==================================");
					String sourceWord = frequency.getWord();
					//System.out.println(sourceWord);
					for (Frequency frequencyWord : frequencyList) {
						String targetWord = frequencyWord.getWord();

						double similarityRate = jw.similarity(sourceWord, targetWord);
						double distance = levenshtein.distance(targetWord, sourceWord);
						Double d = Double.valueOf(distance);
						
						/*
						if (similarityRate > 0.9 && similarityRate != 1 && d.intValue() <= sourceWord.length()) {
							
						}else {
							 similarityRate = jw.similarity(sourceWord, new StringBuilder(targetWord).reverse().toString());
							 distance = levenshtein.distance(targetWord, sourceWord);
							 d = Double.valueOf(distance);
						}*/
						
						
						
						if (similarityRate > 0.9 && similarityRate != 1 && d.intValue() <= sourceWord.length()) {
							Similarity similarity = new Similarity();
							similarity.setCoca(frequencyWord.getCoca());
							similarity.setFrequency(frequencyWord.getFrequency());
							similarity.setFrequencyId(frequency.getId());
							// similarity.setId(null);
							similarity.setLevenshtein(new BigDecimal(distance));
							similarity.setSimilarity(new BigDecimal(similarityRate));
							similarity.setSimilarityWord(frequencyWord.getWord());
							similarityList.add(similarity);
							if (similarityList.size() >= 5000) {
								similarityMapper.insertBatch(similarityList);
								similarityList = new ArrayList<>();
							}
							//System.out.print(targetWord + ";");
						}
					}
					// System.out.println("==================================");
				}
				similarityMapper.insertBatch(similarityList);
				countDownLath.countDown();
			});
		}

		try {
			countDownLath.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instant end = Instant.now();
		
		System.out.println("运行结束,运行时间是:"+Duration.between(start, end).toSeconds());
		
	}

	@Test
	void contextLoads() {

		List<Enwords> enwordsList = enwordsService.list();
		Map<String, Enwords> enwordsListMap = new HashMap<>();
		for (Enwords word : enwordsList) {
			enwordsListMap.put(word.getWord(), word);
		}
		int n = 0;
		List<Coca> cocaList = cocoService.list();
		for (Coca coca : cocaList) {
			if (enwordsListMap.get(coca.getWord()) == null) {

				n++;
			}
		}
		System.out.println("============================");
		System.out.println(n);
		System.out.println("============================");
		/*
		 * List<Frequency> frequencyList = frequencyService.list(); String regex =
		 * "[^a-zA-Z]"; Pattern pattern = Pattern.compile(regex); // 用于存储提取的非英文字符
		 * HashSet<String> nonEnglishChars = new HashSet<>();
		 * 
		 * for(Frequency frequency :frequencyList) {
		 * 
		 * Matcher matcher = pattern.matcher(frequency.getWord());
		 * 
		 * 
		 * // 提取非英文字符 while (matcher.find()) { nonEnglishChars.add(matcher.group()); } }
		 * System.out.println("==================开始输出=================="); for (String
		 * character : nonEnglishChars) { System.out.println(character); }
		 * System.out.println("==================输出Over==================");
		 * 
		 * }
		 */
	}

}
