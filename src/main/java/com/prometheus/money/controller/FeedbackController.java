package com.prometheus.money.controller;

import com.prometheus.money.entity.Feedback;
import com.prometheus.money.res.Res;
import com.prometheus.money.service.IFeedbackService;
import com.prometheus.money.util.ClientIpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 意见反馈 前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2026-02-02
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    @GetMapping
    public String feedbackPage() {
        return "feedback";
    }

    @PostMapping("/submit")
    @ResponseBody
    public Res<String> submitFeedback(@RequestBody Feedback feedback, HttpServletRequest request) {
        String clientAddress = ClientIpAddress.getClientIpAddress();
        feedback.setIp(clientAddress);
        feedback.setCreateTime(LocalDateTime.now());

        boolean saved = feedbackService.save(feedback);
        if (saved) {
            return Res.success("Feedback submitted successfully");
        } else {
            return Res.fail("Submission failed");
        }
    }
}
