package com.prometheus.money.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.res.Res;

@RestController
@RequestMapping("pool")
public class ThreadPoolController {
	@GetMapping("get")
	public Res<String> index() {
		return Res.success("Hello World");
	}
}
