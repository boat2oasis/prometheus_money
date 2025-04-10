package com.prometheus.money.mapper;

import com.prometheus.money.entity.Dialogue;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
public interface DialogueMapper extends BaseMapper<Dialogue> {
	void batchInsert(@Param("list") List<Dialogue> dialogues);
}
