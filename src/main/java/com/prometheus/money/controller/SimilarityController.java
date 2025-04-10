package com.prometheus.money.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Similarity;
import com.prometheus.money.mapper.SimilarityMapper;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IFrequencyService;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2025-01-24
 */
@RestController
@RequestMapping("/similarity")
public class SimilarityController {
	
	@Autowired
	private IFrequencyService frequencyService;
	
	@Autowired
	private SimilarityMapper similarityMapper;

	@GetMapping("/caculateSimilarity")
	public Res<String> caculateSimilarity() {
		Instant start = Instant.now();
		
		int threadNum = 25; 
		
		CountDownLatch countDownLath = new CountDownLatch(threadNum);
		List<Frequency> frequencyList = frequencyService.list();
		System.out.println("=============="+frequencyList.size());
		JaroWinkler jw = new JaroWinkler();
		Levenshtein levenshtein = new Levenshtein();

		ExecutorService executor = Executors.newFixedThreadPool(threadNum);

		int size = frequencyList.size();
		int partitionSize = (int) Math.ceil((double) size / threadNum); // 向上取整
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
		return Res.success("执行成功");
		
	}
}
