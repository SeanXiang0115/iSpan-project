package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 3;
    private final long BLOCK_DURATION_MS = 15 * 60 * 1000; // 15 分鐘

    // 儲存每個 key (例如 email 或 account) 的失敗次數
    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    // 儲存每個 key 的鎖定起始時間
    private final ConcurrentHashMap<String, Long> blockTimeCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        blockTimeCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);

        if (attempts >= MAX_ATTEMPT) {
            blockTimeCache.put(key, System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String key) {
        if (!blockTimeCache.containsKey(key)) {
            return false;
        }
        long blockTime = blockTimeCache.get(key);
        if (System.currentTimeMillis() - blockTime > BLOCK_DURATION_MS) {
            // 鎖定時間已過，自動解除鎖定
            attemptsCache.remove(key);
            blockTimeCache.remove(key);
            return false;
        }
        return true;
    }
}
