package com.example.demo.entity.Reservation;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

import com.example.demo.entity.User;

@Data
@Entity
@Table(name = "stores_info")
public class StoresInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 引用你第一部分的 User Entity

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(nullable = false, length = 2048)
    private String coverImage;

    @Column(nullable = false, length = 20)
    private String storePhone;

    @Column(columnDefinition = "nvarchar(1000)")
    private String description;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, precision = 10, scale = 8)
    private Double latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private Double longitude;

    @Column(nullable = false)
    private Integer timeSlot = 30;

    @Column(nullable = false)
    private Integer timeLimit = 90;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<OpenHour> openHour;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "store_category_mapping", // 資料庫中的中間表名稱
            joinColumns = @JoinColumn(name = "store_id"), // 對應本表 (StoresInfo) 的外鍵
            inverseJoinColumns = @JoinColumn(name = "category_id") // 對應目標表 (Category) 的外鍵
    )
    private List<Category> categories;
}
