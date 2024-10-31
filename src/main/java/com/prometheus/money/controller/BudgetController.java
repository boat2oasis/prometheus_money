package com.prometheus.money.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prometheus.money.entity.Budget;
import com.prometheus.money.entity.transfer.re.BudgetRe;
import com.prometheus.money.entity.transfer.vo.BudgetVo;
import com.prometheus.money.mapper.BudgetMapper;
import com.prometheus.money.mapper.SpentInformationMapper;
import com.prometheus.money.res.Res;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/budget")
public class BudgetController {
	@Autowired
	private BudgetMapper budgetMapper;
	
	@Autowired
	private SpentInformationMapper spentInformationMapper;
	
	@PostMapping("/save")
	public Res<String> save(@RequestBody BudgetRe BudgetRe) {
		System.out.println("===================================================/save");
		Budget entity = new Budget();
		BeanUtils.copyProperties(BudgetRe, entity);
		budgetMapper.insertOrUpdate(entity);
		return Res.success("保存成功");
	}

	@GetMapping("/list")
	public Res<List<BudgetVo>> list() {
		System.out.println("===================================================/list");
		QueryWrapper<Budget> queryWrapper = new QueryWrapper<>();
		List<Budget> resultList = budgetMapper.selectList(queryWrapper);
		List<BudgetVo> resultVoList = new ArrayList<BudgetVo>();
		for(Budget bud:resultList) {
			ZonedDateTime dateFrom = bud.getDateFrom();
	        ZonedDateTime dateTo = bud.getDateTo();
			BigDecimal usedAmount =  spentInformationMapper.getTotalSpent(dateFrom, dateTo);
			
			BigDecimal couldSaveAmount =  spentInformationMapper.getCouldSave(dateFrom, dateTo);
			BudgetVo vo = new BudgetVo();
			BeanUtils.copyProperties(bud, vo);
			
	        // 获取当前日期
	        LocalDate today = LocalDate.now();
	        
	        //将ZonedDateTime 转换为 LocalDate
	        LocalDate dateFromLocal = dateFrom.toLocalDate();
	        LocalDate dateToLocal = dateTo.toLocalDate();
	        
	        // 计算 ZonedDateTime 和当前日期之间的差值
	        long past = 1;
	        long future = 1;
	        if(today.isBefore(dateFromLocal)) {
	        	//日期还没到
	        	//如果
	        	past = 1;
		        future = Math.abs(ChronoUnit.DAYS.between(dateFromLocal, dateToLocal));
	        }else {
	        	past = Math.abs(ChronoUnit.DAYS.between(dateFromLocal, today));
		        future = Math.abs(ChronoUnit.DAYS.between(dateToLocal, today));
	        }
	        
	        if(past == 0) {
	        	past = 1;
	        }
	        if(future == 0) {
	        	future = 1;
	        }
			
			resultVoList.add(vo);
			vo.setUsedAmount(usedAmount);
			vo.setCouldSaveAmount(couldSaveAmount);
			BigDecimal availableAmount = vo.getAmount().subtract(usedAmount);
			vo.setAvailableAmount(availableAmount);
			vo.setAvailableAmountAvg(availableAmount.divide(new BigDecimal(future), 2, RoundingMode.HALF_UP));
		
			vo.setUsedAmountAvg(usedAmount.divide(new BigDecimal(past), 2, RoundingMode.HALF_UP));
		}
		return Res.success(resultVoList);
	}
	
}
