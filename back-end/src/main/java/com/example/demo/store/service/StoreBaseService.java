package com.example.demo.store.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.StoreInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

// 供 StoreInfoService 和 StoreOperationService 共用的基底服務類別，主要提供「取得目前登入店家資訊」的共用邏輯
@RequiredArgsConstructor
public abstract class StoreBaseService {

    protected final UserRepository userRepository;
    protected final StoreInfoRepository storeInfoRepository;

    // 統一取得目前登入店家的邏輯，如果找不到，直接拋出 RuntimeException，簡化子類別邏輯
    public StoresInfo getMyStore() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該登入使用者"));

        return storeInfoRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("找不到該店家的資訊或您不具備店家身分"));
    }
}