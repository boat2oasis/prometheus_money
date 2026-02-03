package com.prometheus.money.controller;

import com.prometheus.money.entity.BestSentence;
import com.prometheus.money.service.IBestSentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.prometheus.money.res.Res;

import java.util.List;

@RestController
@RequestMapping("/best-sentence")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class BestSentenceController {

    @Autowired
    private IBestSentenceService bestSentenceService;

    @GetMapping("/list")
    public Res<IPage<BestSentence>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Res.success(bestSentenceService.list(page, size));
    }

    @GetMapping("/{id}")
    public Res<BestSentence> getById(@PathVariable Integer id) {
        return Res.success(bestSentenceService.getById(id));
    }

    @PostMapping("/add")
    public Res<Object> add(@RequestBody BestSentence sentense) {
        bestSentenceService.save(sentense);
        return Res.success(null);
    }

    @PostMapping("/update")
    public Res<Object> update(@RequestBody BestSentence sentense) {
        bestSentenceService.update(sentense);
        return Res.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public Res<Object> delete(@PathVariable Integer id) {
        bestSentenceService.delete(id);
        return Res.success(null);
    }
}
