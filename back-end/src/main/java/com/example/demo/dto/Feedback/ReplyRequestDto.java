package com.example.demo.dto.Feedback;

import lombok.Data;

@Data
public class ReplyRequestDto {
    private Long id;

    private String reply;

    private Long statusId;

    private Long adminId;

}
