package com.example.demo.service.Feedback;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.Feedback.FeedbackRepository;
import com.example.demo.repository.Feedback.FeedbackStatusRepository;

public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private FeedbackStatusRepository feedbackStatusRepo;

    @Autowired
    private UserRepository userRepo;

}
