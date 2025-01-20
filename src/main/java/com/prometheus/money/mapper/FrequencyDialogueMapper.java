package com.prometheus.money.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prometheus.money.entity.FrequencyDialogue;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Heisenberg
 * @since 2025-01-11
 */
public interface FrequencyDialogueMapper extends BaseMapper<FrequencyDialogue> {
    int batchInsert(@Param("list") List<FrequencyDialogue> dialogues);
}
