package com.example.demo.dto.Feedback;

import lombok.Data;

@Data
public class FeedbackRequestDto {
    private String name;

    private String phone;

    private String email;

    private String contents;

    private Long typeId;
}
