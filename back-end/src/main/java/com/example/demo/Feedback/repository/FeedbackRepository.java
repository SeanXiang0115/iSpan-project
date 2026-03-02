package com.example.demo.Feedback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Feedback.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // 使用 JPQL 語法：f.feedbackStatus 指向 Entity 中的屬性名
    @Query("SELECT f FROM Feedback f WHERE f.feedbackStatus.statusId = :statusId")
    List<Feedback> findByStatus(@Param("statusId") Long statusId);

}
