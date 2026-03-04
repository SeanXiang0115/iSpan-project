package com.example.demo.storeRegis.dto;

import lombok.Data;

@Data
public class StoreRegistrationRequest {
    private String ownerName;
    private String storePhone;
    private String storeAddress;
    private String storeName; // Added based on entity, though frontend might not have it yet
}
