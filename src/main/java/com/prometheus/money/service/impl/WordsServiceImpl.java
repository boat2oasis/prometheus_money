package com.prometheus.money.service.impl;

import com.prometheus.money.entity.Words;
import com.prometheus.money.mapper.WordsMapper;
import com.prometheus.money.service.IWordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-11-22
 */
@Service
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements IWordsService {

}
