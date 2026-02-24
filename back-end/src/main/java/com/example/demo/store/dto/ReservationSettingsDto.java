package com.example.demo.store.dto;

import java.util.List;


import lombok.Data;

@Data
public class ReservationSettingsDto {
    private Integer timeSlot;
    private Integer timeLimit;

    private List<SeatSettingsDto> seatSettings;
    private List<OpenHourDto> openHours;


    @Data
    public static class SeatSettingsDto {
        
    }

    @Data
    public static class OpenHourDto {
        
    }
}
