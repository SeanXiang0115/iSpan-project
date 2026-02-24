package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Feedback.FeedbackRequestDto;

@RestController
@RequestMapping
public class FeedbackController {

    @PostMapping("/feedback")
    public void createFeedback(@RequestBody FeedbackRequestDto dto) {

    }

}
