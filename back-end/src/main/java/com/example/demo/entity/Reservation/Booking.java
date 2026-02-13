package com.example.demo.entity.Reservation;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.entity.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoresInfo store;

    @Column(nullable = false)
    private Integer reservedSeatType;

    @Column
    private Integer guestCount;

    @Column(nullable = false, length = 50)
    private String guestName;

    @Column(nullable = false, length = 20)
    private String guestPhone;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private Boolean status = true;

    @Column(updatable = false, insertable = false, columnDefinition = "datetime2 DEFAULT GETDATE()")
    private LocalDateTime createdAt = LocalDateTime.now();
}
