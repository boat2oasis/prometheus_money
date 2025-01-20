package com.prometheus.money.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prometheus.money.entity.Frequency;
import com.prometheus.money.entity.Words;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-23
 */
public interface FrequencyMapper extends BaseMapper<Frequency> {
	int batchInsert(@Param("wordList") List<Frequency> wordList);
}
