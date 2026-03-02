package com.example.demo.Feedback.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Feedback.dto.FeedbackResponseDto;
import com.example.demo.Feedback.dto.ReplyRequestDto;
import com.example.demo.Feedback.entity.Feedback;
import com.example.demo.Feedback.repository.FeedbackRepository;
import com.example.demo.Feedback.repository.FeedbackStatusRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackAPService {

    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;

    public List<FeedbackResponseDto> findAllFeedbacks() {
        List<Feedback> feedbackList = feedbackRepo.findAll();
        return feedbackList.stream()
                .map(this::convertToDto)
                .toList();

    };

    private FeedbackResponseDto convertToDto(Feedback feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();

        dto.setName(feedback.getName());
        dto.setPhone(feedback.getPhone());
        dto.setEmail(feedback.getEmail());
        dto.setContents(feedback.getContents());
        dto.setReply(feedback.getReply());

        // 2. 處理客訴種類名稱 (Entity 為物件，DTO 為 String)
        if (feedback.getFeedbackTypes() != null) {
            // 這裡假設你的 FeedbackTypes Entity 裡面名稱欄位叫 typeName
            dto.setTypeName(feedback.getFeedbackTypes().getTypeName());
        }

        // 3. 處理處理狀態名稱 (Entity 為物件，DTO 為 String)
        if (feedback.getFeedbackStatus() != null) {
            // 這裡假設你的 FeedbackStatus Entity 裡面名稱欄位叫 statusName
            dto.setStatusName(feedback.getFeedbackStatus().getStatusName());
        }

        // 4. 處理時間轉字串 (LocalDateTime -> String)
        if (feedback.getCreatedAt() != null) {
            dto.setCreatedAt(feedback.getCreatedAt().toString());
        }
        if (feedback.getRepliedAt() != null) {
            dto.setRepliedAt(feedback.getRepliedAt().toString());
        }

        // 5. 處理管理員 ID (從 Admin 物件中取得)
        if (feedback.getAdmin() != null) {
            // 假設你的 Admin Entity 裡的 ID 叫 adminId
            dto.setAdminId(feedback.getAdmin().getId());
        }

        return dto;

    }

}
