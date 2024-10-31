package com.prometheus.money.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prometheus.money.entity.FinancialAccounts;
import com.prometheus.money.entity.transfer.re.FinancialAccountsRe;
import com.prometheus.money.mapper.FinancialAccountsMapper;
import com.prometheus.money.res.Res;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-16
 */
@RestController
@RequestMapping("/financialAccounts")
public class FinancialAccountsController {
	@Autowired
	private FinancialAccountsMapper financialAccountsMapper;

	@PostMapping("/save")
	public Res<String> save(@RequestBody FinancialAccountsRe financialAccountsRe) {
		FinancialAccounts entity = new FinancialAccounts();
		BeanUtils.copyProperties(financialAccountsRe, entity);
		entity.setAccountBalance(financialAccountsRe.getAccountBalance());
		entity.setAccountIcon(financialAccountsRe.getAccountIcon());
		entity.setCreateAt(financialAccountsRe.getCreateAt());
		entity.setAccountName(financialAccountsRe.getAccountName());
		entity.setDefaultAccount(0);
		financialAccountsMapper.insertOrUpdate(entity);
		return Res.success("保存成功");
	}

	@GetMapping("/list")
	public Res<List<FinancialAccounts>> list() {
		QueryWrapper<FinancialAccounts> queryWrapper = new QueryWrapper<>();
		List<FinancialAccounts> resultList = financialAccountsMapper.selectList(queryWrapper);
		return Res.success(resultList);
	}
}
