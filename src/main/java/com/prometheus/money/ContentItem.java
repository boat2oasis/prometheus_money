package com.prometheus.money;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentItem {
    
    @JsonProperty("PoS")
    private String poS;

    @JsonProperty("Rank")
    private String rank;

    @JsonProperty("Freq")
    private String freq;

    // Getters and setters
    public String getPoS() {
        return poS;
    }

    public void setPoS(String poS) {
        this.poS = poS;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}
