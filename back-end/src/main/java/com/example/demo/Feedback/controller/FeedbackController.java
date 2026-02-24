package com.example.demo.Feedback.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Feedback.dto.FeedbackRequestDto;
import com.example.demo.service.Feedback.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public void createFeedback(@RequestBody FeedbackRequestDto dto) {
        feedbackService.createFeedback(dto);
    }

}
