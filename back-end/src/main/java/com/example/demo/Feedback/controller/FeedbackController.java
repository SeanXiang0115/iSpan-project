package com.example.demo.Feedback.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Feedback.dto.FeedbackRequestDto;
import com.example.demo.Feedback.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public void createFeedback(@RequestBody FeedbackRequestDto dto) {
        System.out.println("收到請求了！Name: " + dto.getName());
        feedbackService.createFeedback(dto);
    }

}
