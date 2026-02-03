package com.prometheus.money.service;

import com.prometheus.money.entity.BestSentence;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface IBestSentenceService {
    int save(BestSentence sentense);

    int delete(Integer id);

    int update(BestSentence sentense);

    BestSentence getById(Integer id);

    IPage<BestSentence> list(int page, int size);
}
