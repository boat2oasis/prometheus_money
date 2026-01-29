package com.prometheus.money.service.impl;

import com.prometheus.money.entity.StockQuote;
import com.prometheus.money.mapper.StockQuoteMapper;
import com.prometheus.money.service.IStockQuoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2025-08-30
 */
@Service
public class StockQuoteServiceImpl extends ServiceImpl<StockQuoteMapper, StockQuote> implements IStockQuoteService {

}
