package com.cowrite.project.common.monitor.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisHealthIndicator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Health health() {
        try {
            // 执行一个简单的 Redis 命令来检测 Redis 是否可用
            String response = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(response)) {
                return Health.up().withDetail("Redis", "Available").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("Redis", "Not Available").build();
        }
        return Health.unknown().withDetail("Redis", "Status unknown").build();
    }
}
