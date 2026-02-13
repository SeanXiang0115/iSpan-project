package com.example.demo.entity.Reservation;

import lombok.Data;
import lombok.ToString;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, unique = true, length = 30)
    private String categoryName;

    // 反向關聯，表示這個標籤被哪些店家擁有
    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude // 防止 Lombok 造成記憶體溢出
    @JsonIgnore // 防止 API 回傳時造成無限迴圈
    private List<StoresInfo> stores;
}
