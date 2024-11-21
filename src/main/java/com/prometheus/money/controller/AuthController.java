package com.prometheus.money.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.res.Res;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@GetMapping("/codes")
	public Res<List<String>> info() throws AuthenticationException {

		Map<String, Object> userMap = new HashMap<>();

		userMap.put("id", 0);
		userMap.put("realName", "Vben");
		userMap.put("roles", Collections.singletonList("super")); // 单个角色的 List
		userMap.put("username", "vben");
		List<String> resultString = new ArrayList<String>();
		resultString.add("AC_100100");
		resultString.add("AC_100110");
		resultString.add("AC_100120");
		resultString.add("AC_100010");

		return Res.success(resultString);
		// return jwtUtils.generateToken(userDetails.getUsername());
	}
}
