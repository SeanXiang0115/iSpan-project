package com.example.demo.service.Feedback;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Feedback.FeedbackRequestDto;
import com.example.demo.entity.Feedback.Feedback;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.Feedback.FeedbackRepository;
import com.example.demo.repository.Feedback.FeedbackStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final FeedbackStatusRepository feedbackStatusRepo;
    private final UserRepository userRepo;

    public void createFeedback(FeedbackRequestDto dto) {

        // 檢查是否空值
        if (Strings.isBlank(dto.getEmail())) {
            throw new RuntimeException("Email is required");
        }

        // dto轉換entity
        Feedback feedback = new Feedback();
        feedback.setEmail(dto.getEmail());
        feedback.setName(dto.getName());
        feedback.setPhone(dto.getPhone());
        feedback.setContents(dto.getContents());

        // 寫入資料庫
        feedbackRepo.save(feedback);

    }
}