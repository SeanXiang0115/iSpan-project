package com.example.demo.store.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.UserRepository;
import com.example.demo.store.dto.ReservationSettingsDto;
import com.example.demo.store.entity.OpenHour;
import com.example.demo.store.entity.Seat;
import com.example.demo.store.entity.SeatId;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.OpenHourRepository;
import com.example.demo.store.repository.SeatRepository;
import com.example.demo.store.repository.StoreInfoRepository;

@Service
public class StoreOperationService extends StoreBaseService {

    private final OpenHourRepository openHourRepository;
    private final SeatRepository seatRepository;

    // 手動寫建構子來調用 super(...)
    public StoreOperationService(UserRepository userRepository,
            StoreInfoRepository storeInfoRepository,
            OpenHourRepository openHourRepository,
            SeatRepository seatRepository) {
        super(userRepository, storeInfoRepository);
        this.openHourRepository = openHourRepository;
        this.seatRepository = seatRepository;
    }

    // 更新店家座位、營業時間與時段設定
    @Transactional
    public void updateReservationSettings(ReservationSettingsDto dto) {
        // 1. 取得目前登入的店家資訊
        StoresInfo store = getMyStore();

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