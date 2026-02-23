package com.example.demo.repository.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Feedback.FeedbackStatus;

public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatus, Integer> {

}
