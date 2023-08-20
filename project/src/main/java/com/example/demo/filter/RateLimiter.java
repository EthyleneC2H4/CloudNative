package com.example.demo.filter;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RateLimiter {
    private final ConcurrentHashMap<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastRequestTimes = new ConcurrentHashMap<>();
    private final long timeWindowMillis = 1000;
    private final int requestLimit = 100;

    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();

        // 初始化计数器和上次请求时间
        requestCounts.putIfAbsent(key, new AtomicLong(0));
        lastRequestTimes.putIfAbsent(key, now);

        AtomicLong requestCount = requestCounts.get(key);
        Long lastRequestTime = lastRequestTimes.get(key);

        // 计算时间窗口内的令牌数
        long tokensToAdd = (now - lastRequestTime) / timeWindowMillis;
        if (tokensToAdd > 0) {
            requestCount.set(Math.min(requestLimit, requestCount.get() + tokensToAdd));
            lastRequestTimes.put(key, now);
        }

        // 尝试获取令牌
        if (requestCount.incrementAndGet() <= requestLimit) {
            return true;
        } else {
            return false;
        }
    }
}
