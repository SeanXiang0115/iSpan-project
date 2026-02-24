package com.example.demo.repository.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Feedback.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
