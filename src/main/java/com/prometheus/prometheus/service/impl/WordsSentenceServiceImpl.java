package com.prometheus.prometheus.service.impl;

import com.prometheus.prometheus.entity.WordsSentence;
import com.prometheus.prometheus.mapper.WordsSentenceMapper;
import com.prometheus.prometheus.service.IWordsSentenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-13
 */
@Service
public class WordsSentenceServiceImpl extends ServiceImpl<WordsSentenceMapper, WordsSentence> implements IWordsSentenceService {

}
