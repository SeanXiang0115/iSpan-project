package com.example.demo.store.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.store.dto.ReservationSettingsDto;
import com.example.demo.store.entity.OpenHour;
import com.example.demo.store.entity.Seat;
import com.example.demo.store.entity.SeatId;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.OpenHourRepository;
import com.example.demo.store.repository.SeatRepository;
import com.example.demo.store.repository.StoreInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreOperationService {

    private final UserRepository userRepository;
    private final OpenHourRepository openHourRepository;
    private final SeatRepository seatRepository;
    private final StoreInfoRepository storeInfoRepository;

    // 取得目前登入使用者的 User 實體
    private User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該登入使用者"));
    }

    // 取得目前登入使用者的店家資訊
    @Transactional(readOnly = true)
    public Optional<StoresInfo> getMyStoreInfo() {
        User user = getCurrentUserEntity();
        return storeInfoRepository.findByUser_Id(user.getId());
    }

    // 更新店家座位、營業時間與時段設定
    @Transactional
    public void updateReservationSettings(ReservationSettingsDto dto) {
        // 1. 取得目前登入的店家資訊
        StoresInfo store = getMyStoreInfo()
                .orElseThrow(() -> new RuntimeException("找不到該使用者的店家資訊"));

        Integer storeId = store.getStoreId();

        // 2. 更新店家的基礎設定 (時段、限時)
        store.setTimeSlot(dto.getTimeSlot());
        store.setTimeLimit(dto.getTimeLimit());
        storeInfoRepository.save(store);

        // 3. 更新營業時間 (先刪除，後新增)
        openHourRepository.deleteByStore_StoreId(storeId);
        if (dto.getOpenHours() != null) {
            List<OpenHour> newOpenHours = dto.getOpenHours().stream().map(hDto -> {
                OpenHour oh = new OpenHour();
                oh.setStore(store);
                oh.setDayOfWeek(hDto.getDayOfWeek());
                // 假設前端傳來的是 String "11:00"，需轉換為 LocalTime
                oh.setOpenTime(LocalTime.parse(hDto.getOpenTime()));
                oh.setCloseTime(LocalTime.parse(hDto.getCloseTime()));
                return oh;
            }).toList();
            openHourRepository.saveAll(newOpenHours);
        }

        // 4. 更新座位設定 (先刪除，後新增)
        seatRepository.deleteById_StoreId(storeId);
        if (dto.getSeatSettings() != null) {
            List<Seat> newSeats = dto.getSeatSettings().stream().map(sDto -> {
                Seat seat = new Seat();
                // 處理複合主鍵 SeatId
                SeatId sid = new SeatId(storeId, sDto.getSeatType());
                seat.setId(sid);
                seat.setStore(store);
                seat.setTotalCount(sDto.getTotalCount());
                return seat;
            }).toList();
            seatRepository.saveAll(newSeats);
        }
    }
}