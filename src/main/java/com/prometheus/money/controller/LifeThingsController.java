package com.prometheus.money.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prometheus.money.entity.LifeThings;
import com.prometheus.money.entity.transfer.re.LifeThingsRe;
import com.prometheus.money.entity.transfer.vo.LifeThingsVo;
import com.prometheus.money.mapper.LifeThingsMapper;
import com.prometheus.money.res.Res;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/lifeThings")
public class LifeThingsController {
	@Autowired
	private LifeThingsMapper lifeThingsMapper;

	@PostMapping("/save")
	public Res<String> save(@RequestBody LifeThingsRe lifeThingsRe) {
		LifeThings entity = new LifeThings();
		BeanUtils.copyProperties(lifeThingsRe, entity);
		lifeThingsMapper.insertOrUpdate(entity);
		
		LambdaQueryWrapper<LifeThings> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(LifeThings::getCategoryName,entity.getCategoryName());
		List<LifeThings> resultList = lifeThingsMapper.selectList(queryWrapper);
		for(LifeThings lt:resultList) {
			lt.setCategorySort(entity.getCategorySort());
			lifeThingsMapper.insertOrUpdate(lt);
		}
		
		return Res.success("保存成功");
	}
	
	@GetMapping("/delete/{id}")
	public Res<String> delete(@PathVariable Long id) {
		LifeThings entity = new LifeThings();
		//BeanUtils.copyProperties(lifeThingsRe, entity);
		lifeThingsMapper.deleteById(id);
		
		return Res.success("保存成功");
	}

	@GetMapping("/list")
	public Res<List<LifeThingsVo>> list() {
		
		Set<String> categorySet = new LinkedHashSet<String>();
		
		List<LifeThingsVo> resultDataList = new ArrayList<LifeThingsVo>();
		
		Map<String,LifeThingsVo> categoryMap = new HashMap<String,LifeThingsVo>();
		
		LambdaQueryWrapper<LifeThings> queryWrapper = new LambdaQueryWrapper<>();
		//queryWrapper.orderByAsc(LifeThings::getSort);
		queryWrapper.last("ORDER BY (category_sort IS NULL),category_sort ASC, (sort IS NULL), sort ASC");
		List<LifeThings> resultList = lifeThingsMapper.selectList(queryWrapper);
		for(LifeThings lt:resultList) {
			if(!categorySet.contains(lt.getCategoryName())) {
				categorySet.add(lt.getCategoryName());
				LifeThingsVo ltVo = new LifeThingsVo();
				ltVo.setCategoryName(lt.getCategoryName());
				ltVo.setPrice(BigDecimal.ZERO);
				ltVo.setChildren(new ArrayList<LifeThingsVo>());
				ltVo.setType(0);
				resultDataList.add(ltVo);
				categoryMap.put(lt.getCategoryName(), ltVo);
			}
			LifeThingsVo newlt = new LifeThingsVo();
			newlt.setType(1);
			BeanUtils.copyProperties(lt, newlt);
			//newlt.set
			categoryMap.get(lt.getCategoryName()).getChildren().add(newlt);
			//categoryMap.get(lt.getCategoryName()).getPrice().add(lt.getPrice());
			categoryMap.get(lt.getCategoryName()).setPrice(
					categoryMap.get(lt.getCategoryName()).getPrice().add(lt.getPrice()));
		}

		return Res.success(resultDataList);
	}
}
