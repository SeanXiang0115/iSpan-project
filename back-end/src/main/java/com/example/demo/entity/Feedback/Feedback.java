package com.example.demo.entity.Feedback;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contents;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private String reply;

    @Column(name = "replied_at")
    private LocalDateTime repliedAt;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private FeedbackStatus feedbackStatus;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private FeedbackTypes feedbackTypes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
