package com.prometheus.money.service.impl;

import com.prometheus.money.entity.FinancialAccounts;
import com.prometheus.money.mapper.FinancialAccountsMapper;
import com.prometheus.money.service.IFinancialAccountsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-16
 */
@Service
public class FinancialAccountsServiceImpl extends ServiceImpl<FinancialAccountsMapper, FinancialAccounts> implements IFinancialAccountsService {

}
