package com.example.demo.service.Reservation;

import com.example.demo.dto.Reservation.StoreCreateUpdateDto;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.entity.User;
import com.example.demo.repository.Reservation.StoreInfoRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreInfoService {

    private final StoreInfoRepository storeInfoRepository;
    private final UserRepository userRepository;

    private User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該登入使用者"));
    }

    public Optional<StoresInfo> getMyStoreInfo() {
        User user = getCurrentUserEntity();
        return storeInfoRepository.findByUser_Id(user.getId());
    }

    @Transactional
    public StoresInfo updateMyStoreName(StoreCreateUpdateDto dto) {
        User user = getCurrentUserEntity();
        StoresInfo store = storeInfoRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("找不到該店家的資訊或您不具備店家身分"));

        if (dto.getStoreName() != null) {
            store.setStoreName(dto.getStoreName());
        }

        return storeInfoRepository.save(store);
    }
}
