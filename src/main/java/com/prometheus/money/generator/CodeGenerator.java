package com.prometheus.money.generator;

import java.nio.file.Paths;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {

	private static final String url = "jdbc:mysql://114.132.201.185:3306/coca?useSSL=false&serverTimezone=UTC";

	private static final String username = "root";

	private static final String password = "Aini2026nian!";

	public static void main(String[] args) {
		FastAutoGenerator.create(url, username, password)
				.globalConfig(builder -> builder.author("Heisenberg")
						.outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
						.commentDate("yyyy-MM-dd"))
				.packageConfig(builder -> builder.parent("com.prometheus.money").entity("entity").mapper("mapper")
						.service("service").serviceImpl("service.impl").xml("mapper.xml"))
				// 添加表名称
				.strategyConfig((scanner, builder) -> builder.addInclude("cet")
						.entityBuilder()
						.enableLombok()
						.enableFileOverride()
						.controllerBuilder().enableRestStyle()
						.build())
				.templateEngine(new FreemarkerTemplateEngine()).execute();
	}
}