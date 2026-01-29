package com.prometheus.money.entity;

import java.util.List;

import com.prometheus.money.ContentItem;

public class WordEntry {
    private String headword;
    private List<ContentItem> content;

    // Getters and setters
    public String getHeadword() {
        return headword;
    }

    public void setHeadword(String headword) {
        this.headword = headword;
    }

    public List<ContentItem> getContent() {
        return content;
    }

    public void setContent(List<ContentItem> content) {
        this.content = content;
    }
}
