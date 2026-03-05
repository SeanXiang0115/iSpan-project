package com.example.demo.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.shop.entity.Orders;
import com.example.demo.shop.repository.OrdersRepository;
import com.example.demo.shop.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
@CrossOrigin(origins = "http://localhost:5173")
public class EcpayController {

    @Autowired
    private EcpayService ecpayService;

    @Autowired
    private OrdersRepository ordersRepository;

    // 產生付款表單
    @GetMapping("/pay/{orderId}")
    public ResponseEntity<String> pay(@PathVariable Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));

        String itemName = "Order" + orderId;
        int totalAmount = order.getTotalPrice().intValue();

        String form = ecpayService.generatePaymentForm(orderId, totalAmount, itemName);
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(form);
    }


    // 綠界付款完成後回傳（ReturnURL）
    @PostMapping("/return")
    public ResponseEntity<String> returnUrl(@RequestParam Map<String, String> params) {
        String rtnCode = params.get("RtnCode");
        String merchantTradeNo = params.get("MerchantTradeNo");

        if ("1".equals(rtnCode)) {
            // 付款成功，更新訂單狀態
            // 從 merchantTradeNo 取出 orderId（ORD{orderId}xxxx）
            // 這裡先簡單 log，後續可以更新資料庫
            System.out.println("付款成功：" + merchantTradeNo);
        }

        return ResponseEntity.ok("1|OK"); // 綠界要求回傳 1|OK
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/result")  // ← 改這行
    public ResponseEntity<String> result(@RequestParam Map<String, String> params) {
        System.out.println("綠界回傳參數：" + params);  // ← 加這行

        String rtnCode = params.get("RtnCode");

        String merchantTradeNo = params.getOrDefault("MerchantTradeNo", "");
        
        String redirectUrl;
        if ("1".equals(rtnCode)) {
        redirectUrl = "http://localhost:5173/payment-result?status=success" +
                "&tradeNo=" + merchantTradeNo +
                "&amount=" + params.getOrDefault("TradeAmt", "") +
                "&paymentDate=" + params.getOrDefault("PaymentDate", "").replace(" ", "+");
        } else {
            redirectUrl = "http://localhost:5173/payment-result?status=fail";
        }
        
        String html = "<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
                    "<meta http-equiv='refresh' content='0;url=" + redirectUrl + "'>" +
                    "</head><body></body></html>";
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }
}