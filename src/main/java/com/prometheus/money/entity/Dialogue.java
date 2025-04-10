package com.prometheus.money.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Heisenberg
 * @since 2024-12-01
 */
@Getter
@Setter
@Document(indexName = "dialogues") // 对应ES中的索引名
public class Dialogue implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	@Id
	private Integer id;

	@Field(type = FieldType.Keyword)
	private String series;

	@Field(type = FieldType.Integer)
	private Integer season;

	@Field(type = FieldType.Integer)
	private Integer episode;

	@Field(type = FieldType.Text)
	private String chinese;

	@Field(type = FieldType.Text)
	private String sentence;

	@Field(type = FieldType.Boolean)
	private Boolean review;
}
