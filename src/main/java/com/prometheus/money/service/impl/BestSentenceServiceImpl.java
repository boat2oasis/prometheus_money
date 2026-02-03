package com.prometheus.money.service.impl;

import com.prometheus.money.entity.BestSentence;
import com.prometheus.money.mapper.BestSentenceMapper;
import com.prometheus.money.service.IBestSentenceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BestSentenceServiceImpl implements IBestSentenceService {

    @Autowired
    private BestSentenceMapper bestSentenceMapper;

    @Override
    public int save(BestSentence sentense) {
        if (sentense.getCreateTime() == null) {
            sentense.setCreateTime(new Date());
        }
        return bestSentenceMapper.insertSelective(sentense);
    }

    @Override
    public int delete(Integer id) {
        return bestSentenceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(BestSentence sentense) {
        return bestSentenceMapper.updateByPrimaryKeySelective(sentense);
    }

    @Override
    public BestSentence getById(Integer id) {
        return bestSentenceMapper.selectByPrimaryKey(id);
    }

    @Override
    public IPage<BestSentence> list(int page, int size) {
        Page<BestSentence> pageParam = new Page<>(page, size);
        return bestSentenceMapper.selectAll(pageParam);
    }
}
