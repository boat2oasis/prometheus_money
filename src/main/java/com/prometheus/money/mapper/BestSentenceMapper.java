package com.prometheus.money.mapper;

import com.prometheus.money.entity.BestSentence;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BestSentenceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BestSentence record);

    int insertSelective(BestSentence record);

    BestSentence selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BestSentence record);

    int updateByPrimaryKey(BestSentence record);

    IPage<BestSentence> selectAll(IPage<BestSentence> page);
}
