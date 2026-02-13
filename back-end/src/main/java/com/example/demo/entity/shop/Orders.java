package com.example.demo.entity.shop;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = true)
    private BigDecimal totalPrice;

    @Column(name = "receiver_name", length=(50), nullable = false)
    private String receiverName;


    @Column(name = "receiver_phone", length=(50), nullable = false)
    private String receiverPhone;

    @Column(name = "receiver_address", length=(200), nullable = false)
    private String receiverAddress;
    
    @Column( length=(50))
    private String status;

    @Column(name ="pay_method", length=(50))
    private String payMethod;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "payment_date")
    private Instant paymentDate;


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable= false)
    private User user;


    @PrePersist
    protected void onCreate(){
        if (this.createdAt == null){
            this.createdAt = Instant.now();
        }
        
        //預設status狀態為pending
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

}
