package com.prometheus.money.service.impl;

import com.prometheus.money.entity.Request;
import com.prometheus.money.mapper.RequestMapper;
import com.prometheus.money.service.IRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2025-06-03
 */
@Service
public class RequestServiceImpl extends ServiceImpl<RequestMapper, Request> implements IRequestService {

}
