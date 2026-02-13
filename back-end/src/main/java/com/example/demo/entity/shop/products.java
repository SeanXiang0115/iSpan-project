package com.example.demo.entity.shop;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Products{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", nullable = false, length = 100)    
    private String  productName;

    @Column(name= "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "product_description", nullable = true, length = 500)
    private String productDescription;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant added;

    
    //資料存數的時候，自動填入當下時間
    @PrePersist 
    protected void onCreate(){
        if (this.added == null){
            this.added = Instant.now();
        }
    }

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Stock stock;

}