package com.prometheus.money.monggodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.prometheus.money.monggodb.entity.YourEntity;

public interface YourEntityRepository extends MongoRepository<YourEntity, String> {
    // 可以添加自定义查询方法
    List<YourEntity> findByName(String name);
}
