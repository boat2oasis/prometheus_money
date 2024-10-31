package com.prometheus.money.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prometheus.money.entity.FinancialAccounts;
import com.prometheus.money.entity.SpentInformation;
import com.prometheus.money.entity.transfer.re.SpentInformationRe;
import com.prometheus.money.entity.transfer.vo.SpentInformationVo;
import com.prometheus.money.entity.transfer.vo.TreeSpentInformation;
import com.prometheus.money.enums.NecessaryEnum;
import com.prometheus.money.enums.ProductCategoryEnum;
import com.prometheus.money.enums.ProductPurposeEnum;
import com.prometheus.money.mapper.FinancialAccountsMapper;
import com.prometheus.money.mapper.SpentInformationMapper;
import com.prometheus.money.res.Res;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/spentInformation")
public class SpentInformationController {
	@Autowired
	private SpentInformationMapper spentInformationMapper;
	
	@Autowired
	private FinancialAccountsMapper financialAccountsMapper;

	@PostMapping("/save")
	public Res<String> save(@RequestBody SpentInformationRe spentInformationRe) {
		System.out.println("===================================================/save");
		SpentInformation entity = new SpentInformation();
		BeanUtils.copyProperties(spentInformationRe, entity);
		entity.setCreateAt(LocalDateTime.now());
		spentInformationMapper.insertOrUpdate(entity);

		return Res.success("保存成功");
	}
	
	@GetMapping("/delete")
	public Res<String> delete(Integer id) {
		spentInformationMapper.deleteById(id);
		return Res.success("删除成功");
	}

	@GetMapping("/list")
	public Res<List<TreeSpentInformation>> list() {
		List<SpentInformationVo>  spentGroup = spentInformationMapper.getSpentInfo();
		
		ZonedDateTime fd = ZonedDateTime.now();
		fd.toString();
		
		Map<ZonedDateTime,TreeSpentInformation> resultMap = new HashMap<>();
		List<TreeSpentInformation> resultList = new ArrayList<TreeSpentInformation>();
		for(SpentInformationVo information:spentGroup) {
			TreeSpentInformation  info = new TreeSpentInformation();
			information.setType(0);
			
			
			BeanUtils.copyProperties(information, info);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			info.setToStringSpentDate(information.getSpentDate().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).format(formatter));
			
		
			
			info.setChildren(new ArrayList<SpentInformationVo>());
			info.setOperation(0);
			resultList.add(info);
			
			resultMap.put(information.getSpentDate(), info);
		}
		
		QueryWrapper<SpentInformation> queryWrapper = new QueryWrapper<>();
		List<SpentInformation> resultEntity = spentInformationMapper.selectList(queryWrapper);
		
		QueryWrapper<FinancialAccounts> accountQueryWrapper = new QueryWrapper<>();
		List<FinancialAccounts> accountList = financialAccountsMapper.selectList(accountQueryWrapper);
		
		Map<Integer,String>  accountIdToAccountNameMap = new HashMap<>();
		for(FinancialAccounts account: accountList) {
			accountIdToAccountNameMap.put(account.getId(), account.getAccountName());
		}
		
		for (SpentInformation item : resultEntity) {
			SpentInformationVo newItem = new SpentInformationVo();
			
			BeanUtils.copyProperties(item, newItem);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			newItem.setToStringSpentDate(item.getSpentDate().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).format(formatter).substring(5));
			
			newItem.setUsedForName(ProductPurposeEnum.getValueByCode(item.getUsedFor()));
			newItem.setCategoryName(ProductCategoryEnum.getValueByCode(item.getCategory()));
			BeanUtils.copyProperties(item, newItem);
			SpentInformationVo newItens =new SpentInformationVo();
			BeanUtils.copyProperties(newItem, newItens);
			newItens.setType(1);
			newItens.setOperation(1);
			newItens.setAccountName(accountIdToAccountNameMap.get(newItens.getAccountId()));
			newItens.setNecessaryName(NecessaryEnum.getValueByCode(newItens.getNecessary()));
			resultMap.get(item.getSpentDate()).getChildren().add(newItens);
		}
		return Res.success(resultList);
	}
}
