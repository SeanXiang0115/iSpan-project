package com.example.demo.shop.service;

import com.example.demo.shop.entity.OrderLog;
import com.example.demo.shop.repository.OrderLogRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLogService {

    @Autowired
    private OrderLogRepository orderLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String level, String action, String message, String payload) {

    try{
        OrderLog log = new OrderLog();
        log.setLogLevel(level);
        log.setAction(action);
        log.setMessage(message);
        log.setPayload(payload);
        
        // 使用 saveAndFlush 強制 Hibernate 把資料寫入 SQL
        orderLogRepository.saveAndFlush(log); 
        System.out.println(">>> [Debug] Log 已強制寫入資料庫，ID: " + log.getId());
    }catch (Exception e) {
            System.err.println(">>> [ERROR] Log 寫入失敗: " + e.getMessage());
            e.printStackTrace();
        }
    }
}