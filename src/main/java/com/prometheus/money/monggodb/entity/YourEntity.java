package com.prometheus.money.monggodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "your_collection_name")
@Getter
@Setter
public class YourEntity {

    @Id
    private String id;

    private String name;
    private int age;
}
