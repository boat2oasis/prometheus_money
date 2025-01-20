package com.prometheus.money.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prometheus.money.entity.Words;
import com.prometheus.money.entity.transfer.vo.WordDo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-22
 */
public interface WordsMapper extends BaseMapper<Words> {

	Integer countDistinctWords(@Param("seriesList") List<String> seriesList,
			@Param("seasonList") List<Integer> seasonList, @Param("episodeList") List<Integer> episodeList);
	
	 List<WordDo> getWordCount();
	 
	 
	 int batchInsert(@Param("wordList") List<Words> wordList);
}
