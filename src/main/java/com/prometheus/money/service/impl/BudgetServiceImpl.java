package com.prometheus.money.service.impl;

import com.prometheus.money.entity.Budget;
import com.prometheus.money.mapper.BudgetMapper;
import com.prometheus.money.service.IBudgetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-24
 */
@Service
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements IBudgetService {

}
