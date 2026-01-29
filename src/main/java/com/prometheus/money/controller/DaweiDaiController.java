package com.prometheus.money.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prometheus.money.entity.BigWeiDai;
import com.prometheus.money.mapper.BigWeiDaiMapper;
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
@RequestMapping("/weight")
public class DaweiDaiController {
	@Autowired
	private BigWeiDaiMapper bigWeiDaiMapper;
	
	@PostMapping("/save")
	public Res<String> save(@RequestBody BigWeiDai bigWeiDai) {
		bigWeiDai.setCreateTime(LocalDateTime.now());
		bigWeiDai.setTotalInput(bigWeiDai.getProtein()*4+bigWeiDai.getCarbohydrate()*4+bigWeiDai.getFat()*9);
		
		bigWeiDai.setEffectiveInput((int) Math.round(bigWeiDai.getProtein()*4*0.8+bigWeiDai.getCarbohydrate()*4*0.95+bigWeiDai.getFat()*9));
		bigWeiDai.setBmr(calculateBmr(bigWeiDai.getWeight(),170, "male", LocalDate.of(1995, 11, 11)));
		bigWeiDai.setBmrr(calculateBmrr(bigWeiDai.getWeight(),170, "male", LocalDate.of(1995, 11, 11)));
		bigWeiDai.setCalorieDiff(bigWeiDai.getBmr()+bigWeiDai.getExercise()-bigWeiDai.getEffectiveInput());
		bigWeiDai.setCalorieDifff(bigWeiDai.getBmrr()+bigWeiDai.getExercise()-bigWeiDai.getEffectiveInput());
		
		bigWeiDaiMapper.insertOrUpdate(bigWeiDai);
		return Res.success("保存成功");
	}
	
	@GetMapping("/list")
	public Res<List<BigWeiDai>> list() {

		LambdaQueryWrapper<BigWeiDai> wapper = new LambdaQueryWrapper<BigWeiDai>();
		wapper.orderByAsc(BigWeiDai::getTodayIs);
		List<BigWeiDai> result = bigWeiDaiMapper.selectList(wapper);

		return Res.success(result);
	}
	
	
	public  int calculateBmrr(double weightKg,double height, String gender, LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        double bmr;

        if ("male".equalsIgnoreCase(gender)) {
            bmr = 88.362 + (13.397 * weightKg) + (4.799 * height) - (5.677 * age);
        } else if ("female".equalsIgnoreCase(gender)) {
            bmr = 447.593 + (9.247 * weightKg) + (3.098 * height) - (4.330 * age);
        } else {
            throw new IllegalArgumentException("Gender must be 'male' or 'female'");
        }

        return (int) Math.round(bmr);
    }
	
	public  int calculateBmr(double weightKg,double height, String gender, LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        System.out.println("Age = " + age + " kcal/day");
        double bmr;
        if ("male".equalsIgnoreCase(gender)) {
            bmr = 10 * weightKg + 6.25 * height - 5 * age + 5;
        } else if ("female".equalsIgnoreCase(gender)) {
            bmr = 10 * weightKg + 6.25 * height - 5 * age - 161;
        } else {
            throw new IllegalArgumentException("Gender must be 'male' or 'female'");
        }

        return (int) Math.round(bmr);
    }
}
