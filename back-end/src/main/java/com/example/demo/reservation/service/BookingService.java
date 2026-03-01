package com.example.demo.reservation.service;

import org.springframework.stereotype.Service;

import com.example.demo.reservation.dto.BookingResponseDto;
import com.example.demo.store.repository.OpenHourRepository;
import com.example.demo.store.repository.SeatRepository;
import com.example.demo.store.repository.StoreInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final StoreInfoRepository storeInfoRepository;
    private final OpenHourRepository openHourRepository;
    private final SeatRepository seatRepository;

    // 前台專用：取得指定店家的預約設定
    public BookingResponseDto getStoreReservationSettings(Integer storeId) {

        return null;
    }

}
