package com.prometheus.money.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/english")
public class EnglishLearningController {

    @GetMapping("/high-frequency")
    public String highFrequencyPage(Model model) {
        // Mock data for display
        List<Map<String, Object>> words = new ArrayList<>();

        Map<String, Object> word1 = new HashMap<>();
        word1.put("rank", 1);
        word1.put("word", "the");
        word1.put("frequency", 203739);
        word1.put("cocaRank", 1);
        words.add(word1);

        Map<String, Object> word2 = new HashMap<>();
        word2.put("rank", 2);
        word2.put("word", "be");
        word2.put("frequency", 157758);
        word2.put("cocaRank", 2);
        words.add(word2);

        model.addAttribute("words", words);
        return "english/high-frequency";
    }

    @GetMapping("/collocations")
    public String collocationsPage(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            // Mock search results
            List<Map<String, String>> results = new ArrayList<>();
            Map<String, String> res1 = new HashMap<>();
            res1.put("source", "The Big Bang Theory");
            res1.put("english", "Come to think of it, I would!");
            res1.put("chinese", "细想一下 我还真想呢");
            results.add(res1);

            model.addAttribute("results", results);
            model.addAttribute("query", query);
        }
        return "english/collocations";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            // Mock search results
            List<Map<String, String>> results = new ArrayList<>();
            Map<String, String> res1 = new HashMap<>();
            res1.put("source", "Rick and Morty");
            res1.put("english", "And it's their jobs to decide whether you are or aren't a robot");
            res1.put("chinese", "而他们必须决定你是不是机器人");
            results.add(res1);

            model.addAttribute("results", results);
            model.addAttribute("query", query);
        }
        return "english/search";
    }
}
