package com.example.demo.MapSearch.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapboxService {
    // MapBox Key
    private final String ACCESS_TOKEN = ${mapBox.access-token};

    public double[] getCoordinate(String address) {
        // 加入 country=tw 與 language=zh-Hant 確保台灣地址精準度
        String url = String.format(
                "https://api.mapbox.com/geocoding/v5/mapbox.places/%s.json?access_token=%s&limit=1&country=tw&language=zh-Hant",
                address, ACCESS_TOKEN);

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // 解析 Mapbox 回傳的 GeoJSON 結構
            List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");
            if (!features.isEmpty()) {
                List<Double> center = (List<Double>) features.get(0).get("center");
                // center[0] 是經度 (lng), center[1] 是緯度 (lat)
                return new double[] { center.get(1), center.get(0) };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 轉換失敗
    }
}
