package com.prometheus.money.entity.transfer.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeSpentInformation extends SpentInformationVo{
	private List<SpentInformationVo> children;
}
