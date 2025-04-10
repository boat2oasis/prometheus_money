package com.prometheus.money.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogRecord {
	private String uri;
	private String time;
	private String ip;
	private String requestParam;
}
