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
import com.prometheus.money.entity.LifeThings;
import com.prometheus.money.entity.transfer.re.LifeThingsRe;
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
		return Res.success("保存成功");
	}

	@GetMapping("/list")
	public Res<List<LifeThings>> list() {
		QueryWrapper<LifeThings> queryWrapper = new QueryWrapper<>();
		List<LifeThings> resultList = lifeThingsMapper.selectList(queryWrapper);
		return Res.success(resultList);
	}
}
