package com.prometheus.money.entity.transfer.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo {
	private  String username;
	private String password;
	private Boolean captcha;
	private String selectAccount;
	
}
