package com.prometheus.money.mapper;

import com.prometheus.money.entity.Similarity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @since 2025-01-24
 */
public interface SimilarityMapper extends BaseMapper<Similarity> {
	void insertBatch(@Param("list") List<Similarity> similarityList);
}
