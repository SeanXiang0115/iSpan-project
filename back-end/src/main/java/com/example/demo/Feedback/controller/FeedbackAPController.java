package com.example.demo.Feedback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Feedback.dto.FeedbackResponseDto;
import com.example.demo.Feedback.dto.ReplyRequestDto;
import com.example.demo.Feedback.service.FeedbackAPService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/feedbackList")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackAPController {

    private final FeedbackAPService feedbackAPService;

    @GetMapping
    public List<FeedbackResponseDto> getAllFeedbacks() {
        return feedbackAPService.findAllFeedbacks();
    }

}
