package com.prometheus.money.service.impl;

import com.prometheus.money.entity.Feedback;
import com.prometheus.money.mapper.FeedbackMapper;
import com.prometheus.money.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈表 服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2026-02-02
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
