package com.example.demo.entity.shop;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "available_quantity",nullable = false)
    private Integer availableQuantity;


    @Column(name = "update_at")
    private Instant updateAt;
    //這邊應該要修改SQL為nullable = false


    @OneToOne(fetch =FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    private Products product; //products庫存裡的product


    @PreUpdate
    @PrePersist //在這一筆資料正式塞進資料庫之前，先執行下面這段程式碼。
    protected void onUpdate(){
        this.updateAt = Instant.now();
    }

}
