package com.prometheus.money.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.prometheus.money.entity.Dialogue;

public interface DialogueRepository extends ElasticsearchRepository<Dialogue, Integer> {
	
}