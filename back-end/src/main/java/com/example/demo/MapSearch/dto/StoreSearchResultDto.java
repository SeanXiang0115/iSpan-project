package com.example.demo.MapSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 地圖搜尋結果 DTO
 * 同時用於前端地圖圖釘 (MapContainer) 和店家小卡 (MapStoreCard)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSearchResultDto {

    private Integer storeId;
    private String storeName;
    private String coverImage;
    private String description;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> categories; // 標籤名稱列表
    private String openHoursSummary; // 營業時間摘要字串 (e.g. "Mon-Fri 11:00-21:00")
}
